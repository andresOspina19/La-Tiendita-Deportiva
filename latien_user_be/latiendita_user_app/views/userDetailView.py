"""
from rest_framework import generics, response
from latiendita_user_app.models.user import User
from latiendita_user_app.serializers.userSerializer import UserSerializer

class UserDetailView(generics.ListCreateAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    #   permission_classes = [IsAdminUser]
"""