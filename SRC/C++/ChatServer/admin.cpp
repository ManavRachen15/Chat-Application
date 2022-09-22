#include "admin.h"
#include "ui_admin.h"
#include "mainwindow.h"
#include "server.h"
#include "ui_server.h"
#include "client.h"
#include "ui_client.h"
#include <QApplication>
#include <QSqlDatabase>
#include <QSqlError>
#include <QSqlQuery>
#include <QDebug>

/**
 * @brief Admin::Admin
 * @param usernameL
 * @param passwordL
 * @param parent
 * Brings the username and password from when the user logs in using the database
 */

Admin::Admin(QString usernameL, QString passwordL, QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::Admin)
{
    ui->setupUi(this);
    this->usernameL = usernameL;
    this->passwordL = passwordL;
}

Admin::~Admin()
{
    delete ui;
}

/**
 * @brief Admin::on_pushButton_clicked
 * Allows the admin to press the button to open the server
 */
void Admin::on_pushButton_clicked()
{
    server = new Server(usernameL, passwordL);
    server->show();
}

/**
 * @brief Admin::on_pushButton_2_clicked
 * Allows the admin to press the button to open the client
 */
void Admin::on_pushButton_2_clicked()
{
    client = new Client(usernameL, passwordL);
    client->show();
}

/**
 * @brief Admin::on_pushButton_display_clicked
 * Reads the information from the database for each account and prints in a table
 */
void Admin::on_pushButton_display_clicked()
{
    //QString username = ui->label_user->text();
    //QString password = ui->label_pass->text();
    database = QSqlDatabase::addDatabase("QMYSQL");
    database.setHostName("127.0.0.1");
    database.setUserName("admin");
    database.setPassword("4VFggpYQSuh5");
    database.setDatabaseName("DatabaseChatServer");

    if(database.open()) {
        querymodel = new QSqlQueryModel;
        querymodel->setQuery("SELECT * FROM users");
        ui->tableView->setModel(querymodel);
        QSqlQueryModel model;
        //model.setQuery("SELECT * FROM users WHERE username = " + username);

          //for (int i = 0; i < model.rowCount(); ++i) {
          //    int id = model.record(i).value("id").toInt();
          //    QString name = model.record(i).value("username").toString();
          //    QString email = model.record(i).value("email").toString();
          //    QString phone = model.record(i).value("phone").toString();
          //    qDebug() << id << name << email << phone;
          //    name = username;
          //}
    database.close();
    }
}
