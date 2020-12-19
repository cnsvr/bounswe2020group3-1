# Generated by Django 3.1.3 on 2020-12-12 14:17

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0009_auto_20201212_1633'),
    ]

    operations = [
        migrations.RemoveConstraint(
            model_name='followrequest',
            name='unique_request',
        ),
        migrations.AddConstraint(
            model_name='followrequest',
            constraint=models.UniqueConstraint(fields=('req_from_user', 'req_to_user'), name='unique_request'),
        ),
    ]