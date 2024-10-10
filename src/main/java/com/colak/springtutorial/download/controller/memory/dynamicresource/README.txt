All of these controllers return MediaType.APPLICATION_OCTET_STREAM_VALUE

MediaType.APPLICATION_OCTET_STREAM_VALUE ensures that

- The response will be treated as binary data.
- The client (browser or other HTTP clients) will trigger a download prompt instead of attempting to display the content inline.

Since we are already returning a ResponseEntity<byte[]>, which explicitly handles the byte content, the produces = MediaType.APPLICATION_OCTET_STREAM_VALUE in the @GetMapping is technically optional.