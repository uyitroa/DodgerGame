from django.db import models

# Create your models here.
class Quiz(models.Model):
	question = models.CharField(max_length = 200)
	choices = models.CharField(max_length = 200)
	answer = models.CharField(max_length = 200)

	def getQuestion(self):
		return str(self.question)

	def getChoices(self):
		choice = str(self.choices)
		list_choices = choice.split(',')
		return list_choices

	def getAnswer(self):
		return str(self.answer)

class Account(models.Model):
	username = models.CharField(max_length = 16)
	password = models.CharField(max_length = 16)

	def getUsername(self):
		return str(self.username)

	def getPassword(self):
		return str(self.password)
