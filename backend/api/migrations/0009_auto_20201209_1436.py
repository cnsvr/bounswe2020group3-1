# Generated by Django 3.1.3 on 2020-12-09 11:36

import api.models.tag
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0008_auto_20201206_2028'),
    ]

    operations = [
        migrations.RenameField(
            model_name='profile',
            old_name='share_age',
            new_name='share_birthday',
        ),
        migrations.RemoveField(
            model_name='profile',
            name='age',
        ),
        migrations.AddField(
            model_name='profile',
            name='birthday',
            field=models.DateField(blank=True, null=True),
        ),
        migrations.AddField(
            model_name='tag',
            name='color',
            field=models.IntegerField(default=api.models.tag.random_color),
        ),
    ]
