# Einfache nginx-Website
FROM nginx:alpine

# HTML-Datei ins Standard-Verzeichnis kopieren
COPY verified.html /usr/share/nginx/html/verified.html

# optional: index.html → redirect zu verified.html
RUN echo '<meta http-equiv="refresh" content="0; url=verified.html">' > /usr/share/nginx/html/index.html

EXPOSE 80
