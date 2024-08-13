<template>
    <v-container>
        <v-row justify="center">
            <v-col cols="12" sm="6" md="4">
                <v-card>
                    <v-card-title class="text-h5 text-center">
                        로그인
                    </v-card-title>
                    <v-card-text>
                        <v-form @submit.prevent="doLogin">
                            <v-text-field label="email" v-model="email" type="email" prepend-icon="mdi-email"
                                required></v-text-field>
                            <v-text-field label="패스워드" v-model="password" type="password" prepend-icon="mdi-lock"
                                required></v-text-field>
                            <v-row>
                                <v-col cols="6">
                                    <v-btn color="red" @click="showPasswordModal" block>비밀번호 변경</v-btn>
                                </v-col>
                                <v-col cols="6">
                                    <v-btn type="submit" color="primary" block>로그인</v-btn>
                                </v-col>
                            </v-row>

                        </v-form>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
        <!-- reserPassword가 true가 될때 해당 모달창이 보여짐 -->
         <!-- @update:dialog는 modal컴포넌트가 update:dialog라는 이벤트를 발생시킬 때 실행될 이벤트 핸들러를 정의 -->
          <!-- $event는 자식요소로부터 전달된 값. true/false가 모달로부터 전달 -->
        <ResetPasswordModal v-model="resetPassword" @update:dialog="resetPassword = $event" />
    </v-container>
</template>

<script>
import axios from 'axios';
import { jwtDecode } from 'jwt-decode'
import ResetPasswordModal from './ResetPasswordModal.vue';

export default {
    data() {
        return {
            email: "",
            password: "",
            resetPassword: false,
        }
    },
    components: {
        ResetPasswordModal
    },
    watch: {

    },
    updated() {

    },
    computed: {

    },
    methods: {
        async doLogin() {
            // try catch로 묶어서 login성공시 token을 console.log로 출력
            try {
                const loginData = {
                    email: this.email,
                    password: this.password
                }
                // .env 설정 후 적용하려면 프론트 서버를 껏다 켜야됨
                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member-service/doLogin`, loginData)
                console.log(response.data);
                // localstorage라는 브라우저의 특정 공간에 서버로부터 받아온 토큰값저장
                const token = response.data.result.token;
                const refreshToken = response.data.result.refreshToken;
                console.log(jwtDecode(token));
                const role = jwtDecode(token).role
                localStorage.setItem('token', token)
                localStorage.setItem('refreshToken', refreshToken)
                localStorage.setItem('role', role)
                // this.$router.push("/")
                


                window.location.href = '/'
            } catch (e) {
                console.log(e.response.data);
            }
        },
        showPasswordModal() {
            this.resetPassword = true
        }
    }
}
</script>