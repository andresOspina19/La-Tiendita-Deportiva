from rest_framework import generics, response

from authApp.models.user import User
from authApp.serializers.userSerializer import UserSerializer

class AllUsersView(generics.ListAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    