FROM python:3.9-alpine

ENV ROOT=/app

WORKDIR $ROOT

RUN set -ex \
    && apk update \
    && apk add --no-cache --virtual \
    .build-deps \
    gcc \
    python3-dev \
    libffi-dev \
    jpeg-dev \
    zlib-dev \
    libjpeg \
    openssl-dev \
    postgresql-dev \
    build-base \
    libxml2-dev \
    libxslt-dev \
    && python -m pip install --upgrade pip \
    && pip install --no-cache-dir poetry

ADD pyproject.toml $ROOT

ADD poetry.lock $ROOT

ENV POETRY_VIRTUALENVS_CREATE=false

RUN poetry install --no-dev

ADD . $ROOT

EXPOSE 80

RUN chmod +x ./docker/run.sh

CMD ["./docker/run.sh"]
