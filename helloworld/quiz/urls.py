from django.urls import path
from . import views

urlpatterns = [
	path('<int:ide>/', views.question, name = 'question'),
	path('create/', views.createQuestion, name = 'createQuestion'),
	path('test/', views.saveQuestion, name = 'saveQuestion'),
	path('check/<int:ide>/', views.checkAnswer, name = 'checkAnswer'),
]
