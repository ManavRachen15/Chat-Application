#include "client.h"
#include "ui_client.h"
#include <thread>
/**
 * @brief Client::Client
 * @param usernameL
 * @param passwordL
 * @param parent
 * Brings the username and password from when the user logs in using the database
 * Displays to the user taht the socket is waiting for a connection to a server
 */
Client::Client(QString usernameL, QString passwordL, QWidget *parent) : QMainWindow(parent), ui(new Ui::Client)
{
    ui->setupUi(this);
    this->usernameL = usernameL;
    this->passwordL = passwordL;
    socket = new QTcpSocket(this);

    connect(this, &Client::newMessage, this, &Client::displayMessage);
    connect(socket, &QTcpSocket::readyRead, this, &Client::readSocket);
    connect(socket, &QTcpSocket::disconnected, this, &Client::discardSocket);

    socket->connectToHost(QHostAddress::LocalHost,2462);

    if(socket->waitForConnected())
        ui->statusBar->showMessage("Connected to Server");
    else{
        QMessageBox::critical(this,"QTCPClient", QString("The following error occurred: %1.").arg(socket->errorString()));
        exit(EXIT_FAILURE);
    }
}

Client::~Client()
{
    if(socket->isOpen())
        socket->close();
    delete ui;
}

void Client::readSocket()
{
    QByteArray bfr;

    QDataStream socketStream(socket);

    socketStream.startTransaction();
    socketStream >> bfr;

    if(!socketStream.commitTransaction())
    {
        QString mes = QString("%1 :: Waiting for more data to come..").arg(socket->socketDescriptor());
        emit newMessage(mes);
        return;
    }

    QString header = bfr.mid(0,128);
    QString fileType = header.split(",")[0].split(":")[1];

    bfr = bfr.mid(128);

    if(fileType=="message"){
        QString message = QString("%1 :: %2").arg(socket->socketDescriptor()).arg(QString::fromStdString(bfr.toStdString()));
        emit newMessage(message);
    }
}

void Client::discardSocket()
{
    socket->deleteLater();
    socket=nullptr;

    ui->statusBar->showMessage("Disconnected!");
}


/**
 * @brief Client::displayError
 * @param socketError
 * Displays error for socket connecting to server
 */

void Client::displayError(QAbstractSocket::SocketError socketError)
{
    switch (socketError) {
        case QAbstractSocket::RemoteHostClosedError:
        break;
        case QAbstractSocket::HostNotFoundError:
            QMessageBox::information(this, "QTCPClient", "The host was not found. Please check the host name and port settings.");
        break;
        case QAbstractSocket::ConnectionRefusedError:
            QMessageBox::information(this, "QTCPClient", "The connection was refused by the peer. Make sure QTCPServer is running, and check that the host name and port settings are correct.");
        break;
        default:
            QMessageBox::information(this, "QTCPClient", QString("The following error occurred: %1.").arg(socket->errorString()));
        break;
    }
}

void Client::on_pushButton_sendMessage_clicked()
{
    sendmess();
}

/**
 * @brief Client::sendmess
 * Allows user to send the message from client to server
 */
void Client::sendmess() {

    if(socket)
    {
        if(socket->isOpen())
        {
            QString str = ui->lineEdit_message->text();

            QDataStream socketStream(socket);
            QByteArray header;
            header.prepend(QString("fileType:message,fileName:null,fileSize:%1;").arg(str.size()).toUtf8());
            header.resize(128);

            QByteArray byteArray = str.toUtf8();
            byteArray.prepend(header);

            socketStream << byteArray;

            ui->lineEdit_message->clear();
            ui->textBrowser_receivedMessages->append(str);
        }
        else
            QMessageBox::critical(this,"QTCPClient","Socket doesn't seem to be opened");
    }
    else
        QMessageBox::critical(this,"QTCPClient","Not connected");

}

void Client::displayMessage(const QString& str)
{
    //ui->lineEdit_message->show();
    ui->textBrowser_receivedMessages->append(str);
}
