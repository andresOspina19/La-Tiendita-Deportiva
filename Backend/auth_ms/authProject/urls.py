from django.urls import path
from rest_framework_simplejwt.views import (TokenObtainPairView, TokenRefreshView)
from authApp import views

urlpatterns = [
    path('login/', TokenObtainPairView.as_view()),
    path('refresh/', TokenRefreshView.as_view()),
    path('verifyToken/', views.VerifyTokenView.as_view()),
    path('createUser/', views.UserCreateView.as_view()),
    path('allUsers/', views.AllUsersView.as_view()),
    path('userRetrieveUpdateDelete/<int:pk>', views.UserRetrieveUpdateDeleteView.as_view()),
]
