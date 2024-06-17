create extension if not exists wrappers with schema extensions;
create foreign data wrapper firebase_wrapper
  handler firebase_fdw_handler
  validator firebase_fdw_validator;


create server firebase_server
foreign data wrapper firebase_wrapper
options (
    sa_key '{
        "type": "service_account",
        "project_id": "compsci-computations",
        "private_key_id": "27155cde5bfd4b8d186fd341d7ad8c39ab70959d",
        "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCenRkmJhtg9pja\nqxi8skPMCFKp0X8UshltWw3FnXpvgV2a+J+AUpq9+yRV5JwxT1nA8oO8CsaWzXvs\nhbPbfCazc/DgsEow5DlpoHQaS7aEbCgkUJ5bdTtLxInZg9pNO6DUoFpAd3HxgVCA\n+Xm3llBKDT3HpSCR+EMnCaW7ZfmiBOyH/Hy67DchLOxN5Mctk1PRRi/eenwukoRL\nObDEYwRqbLgw0U0YHk5cAH8DSH4X4cAeTUg2Cjk3owz8i/W5hBJOYhISAcyYPaaO\nrHTwMjhecLeOILtaJzzaxue35g1N9L5YzHDJVY64eGJEFnoEn4nkA/c3iSMuOyQJ\n3yVtGM25AgMBAAECggEALLLvFG4mxxOXjgArr3m5W3V4/jx4yLdMLSI85lTz3Rqi\n63pnUL12uFVfLwmG7uqCy7pMX6DVyEfORaCESDVbnrTszBVCQzb96wN51LNDk1v7\nkAhcKEAIXtjbVD4xNhS4xVMPPB2tMbcUndQwdglZRRUoNOwXGqvRGN5CI2agjATP\nLyzBNrYb2qf9e3X4NKMzZaDoz4cnu7hf1B01Png6w7PRCjq35IuD8tygPCHktjFX\nGoxh6bZiA1bcwk0h6BriaXq2jI4zsT04SsJd2b6m8N0+W1XQtGL2u4NFTXUzEpEi\nLUFqJVdeHmYxULGOzxdbjF9idiFWVAlYbtCV5djx9QKBgQDcbc2DeSY3SSsZvuts\nOM27PxMyYjT7NHzT90Sdf3ldvBgx3JFPTtbF+Xr0gZ7pjjHZQhkFGZxwkpbBEUmB\n3GL8FejDrobpa3BswyRqz2PBRzpZEHBM+DxfyFbrvbIqQozU0N7jLCIWvWM/Zy6Q\n3C/WcOFZ4vHOElUL6SDNAgvSywKBgQC4NaEWH4EhOXdna4n1Y0C1oFHBcnVVsMWl\nuzjEivW/ocAI4sXVBGeyi4PD9sXUy+ogxfNNhPcDGcYWwnC0PsFwM5tcyVkzmspZ\nYnuuT5gwi2g5ayB4OJadRtwlDSvo0GH0nUsjNMA1IdHmbiTWSLdoxSNhFUbOozqZ\nQV9KSaldCwKBgHSW/2TS54vGf62Wdh8/zFGGAzlbCHr2QDA9UWWczyztGD8HUzSH\nlsIQOyCojs0qEOPBOkqgSUahwmtMIX02fISpcfxOi71avpRIgzKbjz/j1NDXCQ8t\nHYHsDU2TgHEp0iOedsXhygYNFjmq8c5dKFBcVFWe3BcLS0CJhnfo5SgRAoGAJyMd\nynl/EfQHrm1mrkAcK3qYZCVRUJdyJrgUY42ykLho+HpV1eBNt910bVosxf1dDQGB\no+KwKHR9ZbCCCYmbx57vT1NoJFKq0zrdVDeM+iNkTW65Q1Gijl/TvfuCbMB5T2Nr\n51r5y1AYLIESSoTAkv3velzu4lBDzvPYrwvfc+kCgYAKaArpA/7LAuU7tVS5jpDt\nz8wlXuMoSsHGJyiV2BwGVr6TP2+oTroZhjV9SDnpKD4JRogYro++RGBJ9rNImWRk\nbPlr5DPoyzUlZTSdLc+uh320eLmnzil/uj2PWHx9NlQ9fyUPxRu/6seVyY5TNGES\nGh7zMzyhCC7NhsS7sC+tYw==\n-----END PRIVATE KEY-----\n",
        "client_email": "firebase-adminsdk-w6mux@compsci-computations.iam.gserviceaccount.com",
        "client_id": "111882342344997677357",
        "auth_uri": "https://accounts.google.com/o/oauth2/auth",
        "token_uri": "https://oauth2.googleapis.com/token",
        "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
        "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-w6mux%40compsci-computations.iam.gserviceaccount.com",
        "universe_domain": "googleapis.com"
    }',
    project_id 'compsci-computations'
);
