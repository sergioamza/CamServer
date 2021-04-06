# CamServer

An idea for a custom CCTV and DVR Running into a Raspberry/Orange/Banana/Whatever.. Pi or another computing system, recycling old webcams.

## CamServer

Scans and serves the image of each cam of the System

### Endpoint

`/`: `index.html` which is located in `src/resources/static` folder

`/cam`: List of cams

`/cam/{id}`: Base64 encoded image

`/cam/{id}/info`: Camera info

## Current work

- Linux daemon

## Future work

- Connect to network cameras
- Provide another module for motion detection, using OpenCV or other Images processing software
- Provide face recognition using some Cloud service
- Integrate other drivers like V4l
- Trigger events (Security, assistance or info) through MQTT server for home devices integration