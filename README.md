# CamServer

An for a custom CCTV and DVR

## CamServer

Scans and serves the image of each cam of the System

### Endpoint

`/`: `index.html` which is located in `src/resources/static` folder

`/cam`: List of cams

`/cam/{id}`: Base64 encoded image

`/cam/{id}/info`: Camera info