<script setup>
import { ref, onMounted, inject } from 'vue'
import { useUserStore } from "../../../stores/user";

const local = inject('local')
const userStore = useUserStore()

const userList = ref([])

const withdraw = async (userId) => {
    if (confirm("정말 회원정보를 삭제하시겠습니까?"))
        await userStore.withdrawal(userId).then(userList.value = userList.value.filter((user) => { user.userId != userId }))

}

onMounted(() => {
    local.get('/user/myPage/userList')
        .then(({ data }) => { userList.value = data.userList })
        .catch((error) => { alert("에러 발생 !!! " + error) })
})


</script>

<template>
    <div class="d-flex align-items-center mt-3">
        <div class="card-group">
            <div class="card p-4 mx-2 d-flex align-items-center" v-for="user in userList" :key="user.userId">
                <img src="@/assets/img/avatar.png" class="card-img-top" />
                <div class="card-body">
                    <h5 class="card-title">{{ user.userId }}</h5>
                    <p class="card-text border-bottom border-2">{{ user.userName }}</p>
                    <p class="card-text border-bottom border-2">{{ user.emailId + '@' + user.emailDomain }}</p>
                    <p class="card-text border-bottom border-2">{{ user.phoneNumber.slice(0, 3) + '-' +
                        user.phoneNumber.slice(3, 7) + '-' +
                        user.phoneNumber.slice(7, 11) }}</p>
                </div>
                <div class="d-flex justify-content-end my-3">
                    <button class="btn btn-danger" type="submit" @click="withdraw(user.userId)">삭제</button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.container-fluid {
    height: 100vh;
    flex-wrap: wrap;
    background-color: #f2f2f2;
    margin: 0;
    padding: 0;
}

.tempclass {
    flex-wrap: wrap;
}

.card {
    min-width: 300px;
    min-height: 350px;
}

.card-img-top {
    max-width: 200px;
    height: 10rem;
    object-fit: cover;
}

.infocard {
    border-radius: 30px;
    border: solid;
    border-color: cornflowerblue;
    background-color: #dcebff;
}

.infobtn {
    background: #ea4c88;
    color: white;
    padding: 1em;
    width: 150px;
    border: none;
}

.people {}

.people:hover {
    background-color: lightblue;
}

.trade {}

.trade:hover {
    background-color: lightgreen;
}

button {
    font-size: 1rem;
    background: #fff;
    color: #4a5568;
    border: 0px solid #073b7e;
    margin: 0.1em;
    transition:
        background 0.2s ease,
        color 0.2s ease,
        box-shadow 0.2s ease,
        transform 0.2s ease;
    box-shadow: 0 0 0 #bee3f8;
    transform: translateY(0);
}

button i {
    color: #a0aec0;
    margin-right: 0.3em;
    transition: all 0.2s ease-out;
}

.multi-button:hover button {
    color: #a0aec0;
}

.multi-button:hover button:hover {
    background: #3f65d5;
    color: #fff;
    box-shadow: 0 0 0.8em 0 rgba(93, 63, 213, 0.8);
    transform: translateY(-0.2em);
}

.multi-button:hover button i {
    color: #cbd5e0;
}

.multi-button:hover button:hover i {
    color: #fed7e2;
    transform: rotate(-10deg);
}
</style>