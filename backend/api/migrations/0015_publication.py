# Generated by Django 3.1.4 on 2020-12-20 20:13

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('api', '0014_auto_20201217_1125'),
    ]

    operations = [
        migrations.CreateModel(
            name='Publication',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=1000)),
                ('publication_year', models.IntegerField(default=0)),
                ('abstract', models.TextField(default='')),
                ('authors', models.TextField(default='')),
                ('journal', models.TextField(default='')),
                ('citation_number', models.IntegerField(default=0)),
                ('profile', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='api.profile')),
            ],
            options={
                'ordering': ['citation_number'],
            },
        ),
    ]