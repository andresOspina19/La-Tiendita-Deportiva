from django.urls import path, include
from django.contrib import admin
from django.contrib.sites.models import Site
from rest_framework_simplejwt.views import (TokenObtainPairView, TokenRefreshView)
from latiendita_user_app.views import AccountListCreateView, AccountRetrieveUpdateDeleteView,UserCreateView
from latiendita_user_app import views

urlpatterns = [
   path('createUser/', UserCreateView.as_view()),
   path('login/', TokenObtainPairView.as_view()),
   path('refresh/', TokenRefreshView.as_view()),
   path('verifyToken/', views.VerifyTokenView.as_view()),
  #  path('user_all/', views.UserDetailView.as_view()),
    path('admin/', admin.site.urls),
    path('listAccounts/', AccountListCreateView.as_view()),
    path('account/<int:pk>/', AccountRetrieveUpdateDeleteView.as_view()),
    path('rest-auth/registration/', include('rest_auth.registration.urls')),
]