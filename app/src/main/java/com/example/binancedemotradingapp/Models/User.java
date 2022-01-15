package com.example.binancedemotradingapp.Models;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    private String sId;
    private String sName;
    private String sSurname;
    private String sEmail;
    private String sPassword;
    private String sPin;
    private String sPhoneNumber;
    // Getters (getteri)
    // Dohvaćanje imena
    public String getName(){
        return sName;
    }
    // Dohvaćanje prezimena
    public String getSurname(){
        return sSurname;
    }
    // Dohvaćanje imena i prezimena
    public String getNameSurname(){
        return sName + " " + sSurname;
    }
    // Dohvaćanje email adrese
    public String getEmail(){
        return sEmail;
    }
    // Dohvaćanje lozinke
    public String getPassword(){
        return sPassword;
    }
    // Dohvaćanje PIN-a
    public String getPIN(){
        return sPin;
    }
    // Dohvaćanje broja mobitela
    public String getPhoneNumber(){
        return sPhoneNumber;
    }
    // Dohvaćanje ID-a
    public String getId(){return sId;}

    // Setters (setteri)
    // Postavljanje imena
    public void setName(String name){
        this.sName = name;
    }
    // Postavljanje prezimena
    public void setSurname(String surname){
        this.sSurname = surname;
    }
    // Postavljanje email adrese
    public void setEmail(String email){
        this.sEmail = email;
    }
    // Postavljanje lozinke
    public void setPassword(String password){
        this.sPassword = password;
    }
    // Postavljanje PIN-a
    public void setPin(String pin){
        this.sPin = pin;
    }
    // Postavljanje broja mobitela
    public void setPhoneNumber(String phonenum){
        this.sPhoneNumber = phonenum;
    }
    // Postavljanje ID-a
    public void setId(String id){this.sId = id;}

    // Provjere
    public boolean registerValidation(){
        // Ako barem jedno od polja nije postavljeno vrati false (dodatne provjera koje polje nije ispravno)
        return checkifEnteredName() && checkifEnteredSurname() && checkifEnteredEmail() && checkifEnteredPassword() && checkifEnteredPin() && checkifEnteredPhoneNum() && checkIfPinHasFourDigits(); // Ako su sva polja postavljena vrati true (izvrši registraciju)
    }
    public boolean loginValidation(){
        return checkifEnteredEmail() && checkifEnteredPassword();
    }
    public boolean editValidation(String hashedPassword, String pin){
        return checkifEnteredName() && checkifEnteredSurname() && checkifEnteredEmail() && checkifEnteredPhoneNum() && checkifEnteredPassword() && checkifEnteredPin() && checkIfPasswordCorrect(hashedPassword) && checkIfPinCorrect(pin);
    }
    public boolean checkIfPasswordCorrect(String hashedPassword){
        return this.sPassword.equals(hashedPassword); // Ovo trenutno nije tocno jer cu morati firebase hash provjerit je li jednak lozinci koja je spremljena u bazi
    }
    public boolean checkIfPinCorrect(String pin){
        return this.sPin.equals(pin);
    }
    // Provjera je li ime postavljeno
    public boolean checkifEnteredName(){
        return !sName.isEmpty();
    }
    // Provjera je li prezime postavljeno
    public boolean checkifEnteredSurname(){
        return !sSurname.isEmpty();
    }
    // Provjera je li email adresa postavljena
    public boolean checkifEnteredEmail(){
        return !sEmail.isEmpty();
    }
    // Provjera je li lozinka postavljena
    public boolean checkifEnteredPassword(){
        return !sPassword.isEmpty();
    }
    // Provjera je li PIN postavljen
    public boolean checkifEnteredPin(){
        return !sPin.isEmpty();
    }
    // Provjera je li postavljen broj mobitela
    public boolean checkifEnteredPhoneNum(){
        return !sPhoneNumber.isEmpty();
    }
    // Provjera ima li pin 4 znamenke
    public boolean checkIfPinHasFourDigits(){
        return sPin.length() == 4;
    }


    public void writeNewUser(User oUser) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://binance-demo-trading-app-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference();
        oUser.setPassword("hidden");
        myRef.child("users").child(String.valueOf(oUser.getId())).setValue(oUser);
        Wallet oWallet = new Wallet();
        oWallet.writeNewWallet(oUser.getId());
    }

    // Rad sa bazom podataka

}
