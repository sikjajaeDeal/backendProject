FROM docker.elastic.co/elasticsearch/elasticsearch:8.12.0

RUN bin/elasticsearch-plugin install --batch analysis-nori
RUN bin/elasticsearch-plugin install --batch https://github.com/hongjunpyo123/elasticsearch-hanhinsam-plugin/releases/download/v0.1/hanhinsam-0.1.zip
