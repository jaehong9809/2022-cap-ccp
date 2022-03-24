from django.db import models
from django.contrib.auth.models import AbstractBaseUser, PermissionsMixin, BaseUserManager

# Create your models here.


class UserManager(BaseUserManager):
    def create_user(self, username, password, age, sex, height, weight):
        user = self.model(username=username, password=password, age=age, sex=sex, height=height, weight=weight)

        user.is_admin = True
        user.is_superuser = True
        user.is_staff = True
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, username, password, age, sex, height, weight):
        user = self.create_user(username=username, password=password,  age=age, sex=sex, height=height, weight=weight)
        user.is_admin = True
        user.is_superuser = True
        user.is_staff = True

        user.save(using=self._db)
        return user


class MyUser(AbstractBaseUser, PermissionsMixin):
    class Meta:
        db_table = "user"

    username = models.CharField(max_length=10, unique=True)
    age = models.IntegerField()
    sex = models.CharField(max_length=5)
    height = models.IntegerField()
    weight = models.IntegerField()
    date_joined = models.DateTimeField(auto_now_add=True)

    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False)

    objects = UserManager()

    USERNAME_FIELD = 'username'
    REQUIRED_FIELDS = ['age', 'sex', 'height', 'weight']

    def __str__(self):
        return self.username
