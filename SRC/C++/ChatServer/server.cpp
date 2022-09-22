#include "server.h"
#include "ui_server.h"
/**
 * @brief Server::Server
 * @param usernameL
 * @param passwordL
 * @param parent
 * Brings the username and password from when the user logs in using the database
 * Shows the user that the server is listening for sockets to get connected to
 */
Server::Server(QString usernameL, QString passwordL, QWidget *parent) : QMainWindow(parent), ui(new Ui::Server)
{
    ui->setupUi(this);
    this->usernameL = usernameL;
    this->passwordL = passwordL;
    serv = new QTcpServer();

    if(serv->listen(QHostAddress::Any, 2462))
    {
       connect(this, &Server::newMessage, this, &Server::displayMessage);
       connect(serv, &QTcpServer::newConnection, this, &Server::newConnection);
       ui->statusBar->showMessage("Server is listening...");
    }
    else
    {
        QMessageBox::critical(this,"QTCPServer",QString("Unable to start the server: %1.").arg(serv->errorString()));
        exit(EXIT_FAILURE);
    }
}

Server::~Server()
{
    foreach (QTcpSocket* socket, conn)
    {
        socket->close();
        socket->deleteLater();
    }

    serv->close();
    serv->deleteLater();

    delete ui;
}

/**
 * @brief Server::newConnection
 * Shows an new established connection to a client
 */
void Server::newConnection()
{
    while (serv->hasPendingConnections())
        appendToSocketList(serv->nextPendingConnection());
}

/**
 * @brief Server::appendToSocketList
 * @param socket
 * Updates the comboBox with all the new sockets that have joined the chat room
 */
void Server::appendToSocketList(QTcpSocket* socket)
{
    conn.insert(socket);
    connect(socket, &QTcpSocket::readyRead, this, &Server::readSocket);
    connect(socket, &QTcpSocket::disconnected, this, &Server::discardSocket);
    //connect(socket, &QAbstractSocket::errorOccurred, this, &MainWindow::displayError);
    ui->comboBox_receiver->addItem(QString::number(socket->socketDescriptor()));
    displayMessage(QString("INFO :: Client with sockd:%1 has just entered the room").arg(socket->socketDescriptor())); //sockd:%1
}

void Server::readSocket()
{
    QTcpSocket* sok = reinterpret_cast<QTcpSocket*>(sender());

    QByteArray bufr;

    QDataStream socketStream(sok);

    socketStream.startTransaction();
    socketStream >> bufr;

    if(!socketStream.commitTransaction())
    {

        QString message = QString("%1 :: Waiting for more data to come..").arg(sok->socketDescriptor());
        emit newMessage(message);
        return;
    }

    QString hdr = bufr.mid(0,128);
    QString file = hdr.split(",")[0].split(":")[1];

    bufr = bufr.mid(128);
    if(file=="message"){
        QString message = QString("%1 :: %2").arg(sok->socketDescriptor()).arg(QString::fromStdString(bufr.toStdString()));
        emit newMessage(message);
    }
}

/**
 * @brief Server::discardSocket
 * Lets the server know when a user has left the chat room
 */
void Server::discardSocket()
{
    QTcpSocket* sok = reinterpret_cast<QTcpSocket*>(sender());
    QSet<QTcpSocket*>::iterator it = conn.find(sok);
    if (it != conn.end()){
        displayMessage(QString("INFO :: A client has just left the room").arg(sok->socketDescriptor()));
        conn.remove(*it);
    }
    refreshComboBox();

    sok->deleteLater();
}

/**
 * @brief Server::displayError
 * @param socketError
 * Displays error for socket connecting to server
 */
void Server::displayError(QAbstractSocket::SocketError socketError)
{
    switch (socketError) {
        case QAbstractSocket::RemoteHostClosedError:
        break;
        case QAbstractSocket::HostNotFoundError:
            QMessageBox::information(this, "QTCPServer", "The host was not found. Please check the host name and port settings.");
        break;
        case QAbstractSocket::ConnectionRefusedError:
            QMessageBox::information(this, "QTCPServer", "The connection was refused by the peer. Make sure QTCPServer is running, and check that the host name and port settings are correct.");
        break;
        default:
            QTcpSocket* socket = qobject_cast<QTcpSocket*>(sender());
            QMessageBox::information(this, "QTCPServer", QString("The following error occurred: %1.").arg(socket->errorString()));
        break;
    }
}

/**
 * @brief Server::on_pushButton_sendMessage_clicked
 * Allows user to decide which particular client or all clients using broadcast they want to message
 */
void Server::on_pushButton_sendMessage_clicked()
{
    QString recieved = ui->comboBox_receiver->currentText();

    if(recieved=="Broadcast")
    {
        foreach (QTcpSocket* socket,conn)
        {
            sendMessage(socket);
        }
    }
    else
    {
        foreach (QTcpSocket* socket,conn)
        {
            if(socket->socketDescriptor() == recieved.toLongLong())
            {
                sendMessage(socket);
                break;
            }
        }
    }
    ui->lineEdit_message->clear();
}

/**
 * @brief Server::sendmess
 * Allows user to decide which particular client or all clients using broadcast they want to message
 */
void Server::sendmess(){
    QString recieved = ui->comboBox_receiver->currentText();

    if(recieved=="Broadcast")
    {
        foreach (QTcpSocket* socket,conn)
        {
            sendMessage(socket);
        }
    }
    else
    {
        foreach (QTcpSocket* socket,conn)
        {
            if(socket->socketDescriptor() == recieved.toLongLong())
            {
                sendMessage(socket);
                break;
            }
        }
    }
    ui->lineEdit_message->clear();

}

/**
 * @brief Server::sendMessage
 * @param socket
 * Allows user to send the message from server to message one or multiple clients
 */
void Server::sendMessage(QTcpSocket* socket)
{
    if(socket)
    {
        if(socket->isOpen())
        {
            QString string = ui->lineEdit_message->text();
            ui->textBrowser_receivedMessages->append(string);
            QDataStream socketStream(socket);

            QByteArray hdr;
            hdr.prepend(QString("fileType:message,fileName:null,fileSize:%1;").arg(string.size()).toUtf8());
            hdr.resize(128);

            QByteArray byteArray = string.toUtf8();
            byteArray.prepend(hdr);

            socketStream << byteArray;
        }
        else
            QMessageBox::critical(this,"QTCPServer","Socket doesn't seem to be opened");
    }
    else
        QMessageBox::critical(this,"QTCPServer","Not connected");
}
/**
 * @brief Server::displayMessage
 * @param str
 * Updates the textBrowser to view the new messages that have been sent
 */
void Server::displayMessage(const QString& str)
{
    ui->textBrowser_receivedMessages->append(str);
    //ui->lineEdit_message->text();
}

void Server::refreshComboBox(){
    ui->comboBox_receiver->clear();
    ui->comboBox_receiver->addItem("Broadcast");
    foreach(QTcpSocket* socket, conn)
        ui->comboBox_receiver->addItem(QString::number(socket->socketDescriptor()));
}
