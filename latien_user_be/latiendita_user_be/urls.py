from django.urls import path
from django.contrib import admin
#from rest_framework_simplejwt.views import (TokenObtainPairView, TokenRefreshView)
from latiendita_user_app.views import TestView, AccountListCreateView, AccountRetrieveUpdateDeleteView
urlpatterns = [
 #   path('login/', TokenObtainPairView.as_view()),
  #  path('refresh/', TokenRefreshView.as_view()),
  #  path('verifyToken/', views.VerifyTokenView.as_view()),
  #  path('user/', views.UserCreateView.as_view()),
  #  path('user_all/', views.UserDetailView.as_view()),
    path('admin/', admin.site.urls),
    path('accounts/', AccountListCreateView.as_view()),
    path('account/<int:pk>/', AccountRetrieveUpdateDeleteView.as_view())
]
