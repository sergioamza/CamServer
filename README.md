# Capture Server

An idea for a custom CCTV and DVR Running into a Raspberry/Orange/Banana/Whatever.. Pi or another computing system, recycling old webcaptures.

Another use could be some detection for a Computer vision application

## capture Server

Scans and serves the image of each capture of the System

## Movement Detector

Scans, process and server the captureeras of remote systems.

### Endpoint

`/`: `index.html` which is located in `src/resources/static` folder

`/capture/list`: List of id of capture devices

`/capture/info`: Info of all capture devices

`/capture/count`: Count of capture devices detected

`/capture/{id}`: Base64 encoded image

`/capture/{id}/info`: capture info

`/capture/{id}/diff`: differences between last and new capture

`/capture/{id}/{prop}/{value}`: Set captureera properties

## Current work

- Linux daemon

## Future work

- Connect to network captureeras
- Provide another module for motion detection, using OpenCV or other Images processing software
- Provide face recognition using some Cloud service
- Integrate other drivers like V4l
- Trigger events (Security, assistance or info) through MQTT server for home devices integration