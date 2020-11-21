# Generated by Django 3.1.3 on 2020-11-21 13:58

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0006_auto_20201121_1551'),
    ]

    operations = [
        migrations.AlterField(
            model_name='event',
            name='event_type',
            field=models.CharField(choices=[('journal submission', '0'), ('academic conference', '1'), ('funded project', '2')], max_length=50),
        ),
    ]
