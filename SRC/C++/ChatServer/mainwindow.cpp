#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <QMessageBox>
#include <QPixmap>
//#include <QFile>
//#include <QTextStream>
#include <QString>
#include <iostream>
#include <fstream>
#include <string>

using namespace std;

/**
 * @brief MainWindow::MainWindow
 * @param parent
 * Sets placeholder text in each lineEdit
 */
MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)

{
    ui->setupUi(this);


    ui->lineEdit_username->setPlaceholderText("Create A Username");
    ui->lineEdit_password->setPlaceholderText("Create A Password");
    ui->lineEdit_email->setPlaceholderText("Enter Your Email");
    ui->lineEdit_phone->setPlaceholderText("Enter Your Phone");
    ui->lineEdit_usernameL->setPlaceholderText("Enter Your Username");
    ui->lineEdit_passwordL->setPlaceholderText("Enter Your Password");
}

MainWindow::~MainWindow()
{
    delete ui;
}

/**
 * @brief MainWindow::on_pushButton_R_clicked
 * Opens the database and allows the user to register for an account using four variables
 */
void MainWindow::on_pushButton_R_clicked()
{
    database = QSqlDatabase::addDatabase("QMYSQL");
    database.setHostName("127.0.0.1");
    database.setUserName("admin");
    database.setPassword("4VFggpYQSuh5");
    database.setDatabaseName("DatabaseChatServer");
    QString username = ui->lineEdit_username->text();
    QString password = ui->lineEdit_password->text();
    QString email = ui->lineEdit_email->text();
    QString phone = ui->lineEdit_phone->text();
    if(username == NULL || password == NULL || email == NULL || phone == NULL){
      QMessageBox::information(this, "Error", "Incorrect Information");

    }
    else if (database.open()){
        QSqlQuery qry;
        qry.prepare("INSERT INTO users (username, password, email, phone)"
                    "VALUES (:username, :password, :email, :phone)");

        qry.bindValue(":username", username);
        qry.bindValue(":password", password                                                                                                                                                     );
        qry.bindValue(":email", email);
        qry.bindValue(":phone", phone);

        if(qry.exec()) {
            QMessageBox::information(this, "Inserted", "Data Inserted Successfully");

        }
        else {
            QMessageBox::information(this, "Not Inserted", "Data Not Inserted");
        }
    }
    else {
        QMessageBox::information(this, "Not Connected", "Database Is Not Connected");

    }
}

/**
 * @brief MainWindow::on_pushButton_clicked
 * Opens the database and allows the user to login to their account using two variables
 */
void MainWindow::on_pushButton_clicked()
{

    database = QSqlDatabase::addDatabase("QMYSQL", "MyConnect");
    database.setHostName("127.0.0.1");
    database.setUserName("admin");
    database.setPassword("4VFggpYQSuh5");
    database.setDatabaseName("DatabaseChatServer");

    QString usernameL = ui->lineEdit_usernameL->text();
    QString passwordL = ui->lineEdit_passwordL->text();

    if(usernameL == "admin" && passwordL == "admin"){
        QMessageBox::information(this, "Success", "Login to Admin");
        hide();
        admin = new Admin(usernameL, passwordL);
        admin->show();
    }

    if(usernameL == NULL || passwordL == NULL){
      QMessageBox::information(this, "Error", "Incorrect Information");

    }
    if (database.open()){
        //QMessageBox::information(this, "Database Success", "Database Connection Successful");

        QSqlQuery query(QSqlDatabase::database("MyConnect"));

        query.prepare(QString("SELECT * FROM users WHERE username = :username AND password = :password"));

        query.bindValue(":username", usernameL);
        query.bindValue(":password", passwordL);

        if(!query.exec()) {
            QMessageBox::information(this, "Failed", "Query Failed To Execute");
        }
        else
        {
            while (query.next()) {
                QString usernameFromDB = query.value(1).toString();
                QString passwordFromDB = query.value(2).toString();

                if(usernameFromDB == usernameL && passwordFromDB == passwordL)
                {
                    QMessageBox::information(this, "Success", "Login Successful");
                    hide();
                    homepage = new Homepage(usernameL, passwordL);
                    homepage->show();

                }
                else
                {
                    QMessageBox::information(this, "Failed", "Login Failed");
                }
            }
        }

    }
    else{
        QMessageBox::information(this, "Database Failed", "Database Connection Failed");
    }
}
