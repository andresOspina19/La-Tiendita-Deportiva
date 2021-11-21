from rest_framework import views, status
from rest_framework.response import Response
from latiendita_user_app.models import User
from latiendita_user_app.serializers import UserSerializer
from rest_framework import generics
from django.contrib.auth.decorators import login_required
from django.shortcuts import render,redirect,reverse
from django.views import View
from django.http import HttpResponse
#from django.contrib.auth.models import User #Importar modelo de usuario (registrarse)
from rest_framework_simplejwt.serializers import TokenObtainPairSerializer

# List all accounts and Create a account
class AccountListCreateView(generics.ListCreateAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer

# Retrieve, Update, Delete a account
class AccountRetrieveUpdateDeleteView(generics.RetrieveUpdateDestroyAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    
class UserCreateView(views.APIView):
    def post(self, request, *args, **kwargs):
        serializer = UserSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()
        tokenData = {"username":request.data["username"],
                    "password":request.data["password"]}
        tokenSerializer = TokenObtainPairSerializer(data=tokenData)
        tokenSerializer.is_valid(raise_exception=True)
        return Response(tokenSerializer.validated_data, status=status.HTTP_201_CREATED)