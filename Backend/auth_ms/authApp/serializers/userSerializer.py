from rest_framework import serializers
from authApp.models.user import User

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'username', 'password', 'name', 'email', 'address', 'phoneNumber','is_active']

    def to_representation(self, obj):
        user = User.objects.get(id=obj.id) 
        return {
                    'id': user.id, 
                    'username': user.username,
                    'name': user.name,
                    'email': user.email,
                    'address': user.address,
                    'phoneNumber': user.phoneNumber,
                    'is_active': user.is_active,
        }

