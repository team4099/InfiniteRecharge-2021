#include <Wire.h>

void setup() {
  // put your setup code here, to run once:
  Wire.begin(4);                // join i2c bus with address #8
  Wire.onReceive(receiveData); // register incoming data
  Wire.onRequest(sendData); //register a request for data
  Serial.begin(9600);           // start serial for output
}

void loop() {
  // put your main code here, to run repeatedly:

}

// function that executes whenever data is received from master
// this function is registered as an event, see setup()
void receiveData()
{
   while (Wire.available()) {
    char c = Wire.read();
    Serial.print(c);
   }
Serial.println("");
}

// function that executes whenever data is requested from master
// this function is registered as an event, see setup()
void sendData()
{
   Wire.write("From Arduino");
}
