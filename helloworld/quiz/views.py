from django.shortcuts import render
from django.http import HttpResponse
from .models import Quiz, Account
# Create your views here.

def question(request, ide):
	q = Quiz.objects.get(pk = ide)
	ques = q.getQuestion()
	choices = q.getChoices()
	return render(request, 'showquestion.html', { 'id_question' : ide, 'string_question' : ques, 'choices' : choices})

def createQuestion(request):
	return render(request, 'createquestion.html')

def saveQuestion(request):
	question = request.POST['question']
	choices = request.POST['choices']
	answer = request.POST['answer']
	quiz = Quiz(question = question, choices = choices, answer = answer)
	quiz.save()
	return HttpResponse('created')

def home(request):
	my_number = []
	for x in range(1, Quiz.objects.count() + 1):
		my_number.append(x)
	return render(request, 'home.html', {'my_number' : my_number})

def checkAnswer(request, ide):
	answer = str(request.POST['answer'])
	if(answer == Quiz.objects.get(pk = ide).getAnswer()):
		return HttpResponse("Correct")
	else:
		return HttpResponse("Wrong")

def loginPage(request):
	return render(request, 'login.html')

def verify(request):
	username = str(request.POST['loginstuff'])
	password = str(request.POST['mdp'])
	list_username = []
	list_password = []
	for x in range(Account.objects.count()):
		list_username.append(Account.objects.get(pk = x + 1).getUsername())
		list_password.append(Account.objects.get(pk = x + 1).getPassword())

	if(username in list_username and password in list_password):
		return HttpResponse("Logged in")
	else:
		return HttpResponse("HACKER")
