#include <pgmspace.h>
 
#define SECRET
 
const char WIFI_SSID[] = "R4v3n_S1y";               //
const char WIFI_PASSWORD[] = "B4ll4d4*1";           //
 
#define THINGNAME "ESP8266"
 
int8_t TIME_ZONE = -5; //NYC(USA): -5 UTC
 
const char MQTT_HOST[] = "a2axh58ph1yz86-ats.iot.us-east-1.amazonaws.com";
 
 
static const char cacert[] PROGMEM = R"EOF(
-----BEGIN CERTIFICATE-----
MIIDQTCCAimgAwIBAgITBmyfz5m/jAo54vB4ikPmljZbyjANBgkqhkiG9w0BAQsF
ADA5MQswCQYDVQQGEwJVUzEPMA0GA1UEChMGQW1hem9uMRkwFwYDVQQDExBBbWF6
b24gUm9vdCBDQSAxMB4XDTE1MDUyNjAwMDAwMFoXDTM4MDExNzAwMDAwMFowOTEL
MAkGA1UEBhMCVVMxDzANBgNVBAoTBkFtYXpvbjEZMBcGA1UEAxMQQW1hem9uIFJv
b3QgQ0EgMTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALJ4gHHKeNXj
ca9HgFB0fW7Y14h29Jlo91ghYPl0hAEvrAIthtOgQ3pOsqTQNroBvo3bSMgHFzZM
9O6II8c+6zf1tRn4SWiw3te5djgdYZ6k/oI2peVKVuRF4fn9tBb6dNqcmzU5L/qw
IFAGbHrQgLKm+a/sRxmPUDgH3KKHOVj4utWp+UhnMJbulHheb4mjUcAwhmahRWa6
VOujw5H5SNz/0egwLX0tdHA114gk957EWW67c4cX8jJGKLhD+rcdqsq08p8kDi1L
93FcXmn/6pUCyziKrlA4b9v7LWIbxcceVOF34GfID5yHI9Y/QCB/IIDEgEw+OyQm
jgSubJrIqg0CAwEAAaNCMEAwDwYDVR0TAQH/BAUwAwEB/zAOBgNVHQ8BAf8EBAMC
AYYwHQYDVR0OBBYEFIQYzIU07LwMlJQuCFmcx7IQTgoIMA0GCSqGSIb3DQEBCwUA
A4IBAQCY8jdaQZChGsV2USggNiMOruYou6r4lK5IpDB/G/wkjUu0yKGX9rbxenDI
U5PMCCjjmCXPI6T53iHTfIUJrU6adTrCC2qJeHZERxhlbI1Bjjt/msv0tadQ1wUs
N+gDS63pYaACbvXy8MWy7Vu33PqUXHeeE6V/Uq2V8viTO96LXFvKWlJbYK8U90vv
o/ufQJVtMVT8QtPHRh8jrdkPSHCa2XV4cdFyQzR1bldZwgJcJmApzyMZFo6IQ6XU
5MsI+yMRQ+hDKXJioaldXgjUkK642M4UwtBV8ob2xJNDd2ZhwLnoQdeXeGADbkpy
rqXRfboQnoZsG4q5WTP468SQvvG5
-----END CERTIFICATE-----
)EOF";
 
 
// Copy contents from XXXXXXXX-certificate.pem.crt here ▼
static const char client_cert[] PROGMEM = R"KEY(
-----BEGIN CERTIFICATE-----
MIIDWTCCAkGgAwIBAgIUKbhzcgkRg9S2BAjbmEUNd2IsT08wDQYJKoZIhvcNAQEL
BQAwTTFLMEkGA1UECwxCQW1hem9uIFdlYiBTZXJ2aWNlcyBPPUFtYXpvbi5jb20g
SW5jLiBMPVNlYXR0bGUgU1Q9V2FzaGluZ3RvbiBDPVVTMB4XDTIzMDcwMjAxNTQw
MVoXDTQ5MTIzMTIzNTk1OVowHjEcMBoGA1UEAwwTQVdTIElvVCBDZXJ0aWZpY2F0
ZTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAOtqO2e8OgqMTzNBPkML
Uaf62RciKjUQo0CmZT4BMq5bnIBnjAWVRmZKGkrh5hwv8EVTzrlyyxidWL4Sp0NP
bDVGo9vQhdHiXghvue0vyGT5eqVpK3Hlc0mvoRRyNquilnE/s5VkQWzpNOqMgH5h
jNgRmpqlyZM2fVZrnPaiodXoQnkTs7pSegpsvgRXRE7MuDsI15BUMUwZZEUPmxQC
G1J2+ECQEPQ9VpFPrsjFF7ZOyErQEKVEs9eCeo3mbQneHBdS2C5g5LvVL8FBSjiL
7v1eTfAA+SjVejD+A0nEhg33/W6UwgUaRXge9fGX51sDloxddNmWgg1pg9lFzFrZ
Cz0CAwEAAaNgMF4wHwYDVR0jBBgwFoAUAj/vTE6/DS/7NTTYE1qxr5wzcWIwHQYD
VR0OBBYEFHBA0wggu9JuFvz7cCTqhoC2kaUWMAwGA1UdEwEB/wQCMAAwDgYDVR0P
AQH/BAQDAgeAMA0GCSqGSIb3DQEBCwUAA4IBAQCTe9LHfmcZnxJ2xvxwouzL5zE5
rZsyEIzP4iK/RUqjfUiRu1PiD69fSF3x1Yk/J6btSkpjLmTbNYCh7gLpxYL2TpLC
Zi6ScudySY3y9Bv8pqBK4f6GoqjzYwp3FQYjdsqkKfzzWqK/TxSEtEj84Lua8wkc
MWdc1iV/Xv4tqhm+Nws75UTmmG5xMbKcOKl973p8Yq3ocn74APEipoGOP1xjAM2d
WV+d6pi1cYvIpCVdGpW6qjvKWw6/z2giUqNHJYotF/YLznME15LPiNUGK29Rv/Iv
vNVFQTK6lgnP6i6f45d/ZEGCFOopHyZSHpVurjsbTdgT32zpAOjSPaIJ1vfw
-----END CERTIFICATE-----

)KEY";
 
 
// Copy contents from  XXXXXXXX-private.pem.key here ▼
static const char privkey[] PROGMEM = R"KEY(
-----BEGIN RSA PRIVATE KEY-----
MIIEpQIBAAKCAQEA62o7Z7w6CoxPM0E+QwtRp/rZFyIqNRCjQKZlPgEyrlucgGeM
BZVGZkoaSuHmHC/wRVPOuXLLGJ1YvhKnQ09sNUaj29CF0eJeCG+57S/IZPl6pWkr
ceVzSa+hFHI2q6KWcT+zlWRBbOk06oyAfmGM2BGamqXJkzZ9Vmuc9qKh1ehCeROz
ulJ6Cmy+BFdETsy4OwjXkFQxTBlkRQ+bFAIbUnb4QJAQ9D1WkU+uyMUXtk7IStAQ
pUSz14J6jeZtCd4cF1LYLmDku9UvwUFKOIvu/V5N8AD5KNV6MP4DScSGDff9bpTC
BRpFeB718ZfnWwOWjF102ZaCDWmD2UXMWtkLPQIDAQABAoIBAQC+bXdSSWMV5zul
tU/9Aany3KpA2UUVGblE8ZSR3Hw3qClngNs2JPMGiUjNbAAsquaL3egqAN6isZz4
W7RrcVKRoeuoQvwMtDTmVCIs5+sNKvV32iPnhPiGEYiWNnFc0y8ouqO045080UQZ
ur+x+4zntw29rP/qkjg+8NvOce3BTgs+dga1DphgqBNHBHtP6wq5JiWZ164peKkp
E/tbhLJOm08IMDGbHVXnsMDY+zvLsMylYFEnhOLsSSdLkYdfc8x+PtXnuc9tfodd
D+/vRee/bVavyuzX54ITZr4d+5xtpJi/EDYgW3VuvcJHY+rHHifSdQnnLJLO7BJx
cSH9Qh4NAoGBAPrD0WqhTVnIxSsz9fgDlFrNtf6VApIjb25eKVUjZNs98PaVNF0m
zimlrIBGa0xQkTTlOn9DGgoPrSrbuEvKRXONmjTAXz2Vj9ME8MaGqmuTrMQZtQ78
KVIP6p4mb02qeZcwPjZS6ltvS/8W5zskHmZdD37G8ahGYm3UtppF+Sk3AoGBAPBU
YM3VCSzpfUCeenIlfol0/OW4fmQyKxISH4xEYL5lUv/e3jx0/IyfHSUuSrlqIc4R
gHZshlDJOnxdmxn53Vskr+5UZk2ZVi/rakgyF3ysv6curIy5dKDNN1W4Z9MOO5Qd
qRATW7+CYPw1erK4DBw7s4Xp0Tb/vXYUJkEkLVkrAoGAWph2RxzBckeLnEEogxLf
/TJGpxQ0mpF8x5XzPtQLsv2c2aksAt9rKja1rJ5EU4zj+QU77hBTDwF84XgCDDem
gcnNxXMetYQcMvOc/UxXs4flqe9Tr3dAgmpHrB1cpuWJCisevseT7ba28RDYN1Ck
Pv0uBPJhChdrphxsYC4Zh18CgYEAw2LGJPF6+TGGrB9ryxtiiZw78wOQdiM3UwS6
802wHXrBBNcLdLljjWvaxRyikaWQgDlIaH8In+tJikyLyY2TggMMqUWiupncMS9C
hBl9cNX9Jo1SqUIpaIYO47j9Z+BdXajThwdGAyebc9CCxYPODLIY3NlgR0eZn8L3
6uPfnmsCgYEA2MuPzN6crOgUfHRx6EY1VZnuN6ubEkLw94eh1h234DgfSnUrfQd6
iCaBrlrgt8evkFkHyJx49HE1W2Z97lz7Ts8w5wiJh5w05sboGV8UUNMydTgng2X8
IYNQ9jlhBT1khuYsK4kCx7FgLxdxSp5iPMEQ17mLOXj4FIVj0upVc7A=
-----END RSA PRIVATE KEY-----

)KEY";