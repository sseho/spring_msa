<template>
    <v-container>
        <v-row justify="center">
            <!-- 화면크기가 small이상(스마트폰, 태블릿)일때 : sm -->
            <!-- 화면크기가 medium 이상(데스크탑) 일때 : md -->
            <v-col cols="12" sm="6" md="6">
                <v-card>
                    <v-card-title class="text-h5 text-center">
                        회원가입
                    </v-card-title>
                    <v-card-text>
                        <v-form @submit.prevent="memberCreate">
                            <v-text-field label="이름" v-model="name" prepend-icon="mdi-account" required>

                            </v-text-field>
                            <v-text-field label="email" v-model="email" type="email" prepend-icon="mdi-email" required>

                            </v-text-field>
                            <v-text-field label="패스워드" v-model="password" type="password" prepend-icon="mdi-lock"
                                required>

                            </v-text-field>
                            <v-text-field label="도시" v-model="city" prepend-icon="mdi-city">

                            </v-text-field>
                            <v-text-field label="상세주소" v-model="street" prepend-icon="mdi-home">

                            </v-text-field>
                            <v-text-field label="우편번호" v-model="zipcode" prepend-icon="mdi-mailbox">

                            </v-text-field>
                            <!-- block은 부모컴테이너 너비만큼을 꽉채우는 것 -->
                            <v-btn type="submit" color="primary" block>등록</v-btn>
                        </v-form>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import axios from 'axios';
export default {
    data() {
        return {
            name: "",
            email: "",
            password: "",
            city: "",
            street: "",
            zipcode: "",
        }
    },
    watch: {

    },
    updated() {

    },
    computed: {

    },
    methods: {
        async memberCreate() {
            try {
                const registerData = {
                    name: this.name,
                    email: this.email,
                    password: this.password,
                    address: {
                        city: this.city,
                        street: this.street,
                        zipcode: this.zipcode,
                    }
                }
                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member-service/member/create`, registerData)
                this.$router.push("/")
                console.log(response.data);

            } catch (e) {
                console.log(e.response.data);
            }

        }
    }
}
</script>