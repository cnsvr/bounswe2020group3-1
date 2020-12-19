from rest_framework import serializers
from api.models.following import Following, FollowRequest


class FollowingSerializer(serializers.HyperlinkedModelSerializer):
    """
    Following serializer
    """
    from_user = serializers.IntegerField(source='from_user.id')

    class Meta:
        model = Following
        fields = ("id", "from_user", "created")

    def to_native(self, value):
        return self.parent.to_native(value)


class FollowerSerializer(serializers.HyperlinkedModelSerializer):
    """
    Follower serializer
    """
    to_user = serializers.IntegerField(source='to_user.id')

    class Meta:
        model = Following
        fields = ("id", "to_user", "created")

    def to_native(self, value):
        return self.parent.to_native(value)


class FollowSerializer(serializers.HyperlinkedModelSerializer):
    """
    Follow serializer
    """
    from_user = serializers.IntegerField(source='from_user.id')
    to_user = serializers.IntegerField(source='to_user.id')

    class Meta:
        model = Following
        fields = ("from_user", "to_user", "created")


class FollowRequestSerializer(serializers.HyperlinkedModelSerializer):
    """
    Follow request serializer.
    """
    req_from_user = serializers.IntegerField(source='req_from_user.id')
    req_to_user = serializers.IntegerField(source='req_to_user.id')

    class Meta:
        model = FollowRequest
        fields = ("req_from_user", "req_to_user", "created")


class IncomingFollowRequestSerializer(serializers.HyperlinkedModelSerializer):
    """
    Follow request serializer.
    """
    from_user = serializers.IntegerField(source='req_from_user.id')

    class Meta:
        model = FollowRequest
        fields = ("id", "from_user", "created")

    def to_native(self, value):
        return self.parent.to_native(value)