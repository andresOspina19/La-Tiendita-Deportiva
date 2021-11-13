from rest_framework import views, status
from rest_framework.response import Response
from latiendita_user_app.models import User
from latiendita_user_app.serializers import UserSerializer
from rest_framework import generics

class TestView(views.APIView):

    def get(self, request):
        code = status.HTTP_200_OK
        data = {"message": "Hola mundo, este es mi primer endpoint :0"}
        return Response(data, status=code)

    def post(self, request):
        print("Creando Usuario")
        print("Información recibida: ", request.data)
        code = status.HTTP_201_CREATED
        return Response(status=code)


class AccountView(views.APIView):
    
    def get(self, request):
        queryset = User.objects.all() # Obtener objetos base de datos
        serialized = UserSerializer(queryset, many=True) # Convertir objetos de la base de datos a JSON
        return Response(serialized.data, status=status.HTTP_200_OK)


# List all accounts and Create a account
class AccountListCreateView(generics.ListCreateAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer

# Retrieve, Update, Delete a account
class AccountRetrieveUpdateDeleteView(generics.RetrieveUpdateDestroyAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer