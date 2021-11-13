from django.contrib import admin
from .models.user import User


# Register your models here.
class AccountAdmin(admin.ModelAdmin):
    list_per_page = 20
    list_display = ('id', 'username', 'password', 'name', 'email')
    search_fields = ['email']
    list_filter = ('is_active','email')

admin.site.register(User, AccountAdmin)
