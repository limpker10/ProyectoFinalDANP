#include <pgmspace.h>
 
#define SECRET
 
const char WIFI_SSID[] = "You SSID";               //
const char WIFI_PASSWORD[] = "Your Passwd";           //
 
#define THINGNAME "ESP8266"
 
int8_t TIME_ZONE = -5; //NYC(USA): -5 UTC
 
const char MQTT_HOST[] = "a2axh58ph1yz86-ats.iot.us-east-1.amazonaws.com";
 
 
static const char cacert[] PROGMEM = R"EOF(
Certificate CA1 or 3
)EOF";
 
 
// Copy contents from XXXXXXXX-certificate.pem.crt here ▼
static const char client_cert[] PROGMEM = R"KEY(
Client Certificate

)KEY";
 
 
// Copy contents from  XXXXXXXX-private.pem.key here ▼
static const char privkey[] PROGMEM = R"KEY(
Private certificate

)KEY";