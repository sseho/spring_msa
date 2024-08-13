<template>
    <v-dialog max-width="500px">
        <v-card>
            <v-card-title class="text-h5 text-center">
                비밀번호 변경하기
            </v-card-title>
            <v-card-text>
                <v-form @submit.prevent="resetPassword">
                    <v-text-field label="email" v-model="email" type="email" prepend-icon="mdi-email" required></v-text-field>
                    <v-text-field label="기존 비밀번호" v-model="asIsPassword" type="password" prepend-icon="mdi-lock" required></v-text-field>
                    <v-text-field label="신규 비밀번호" v-model="toBePassword" type="password" prepend-icon="mdi-lock" required></v-text-field>
                    <v-btn type="submit" color="primary" block>비밀번호 재설정 요청</v-btn>
                    <v-btn color="red" @click="closeModal" block>닫기</v-btn>
                    
                </v-form>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            email:"",
            asIsPassword:"",
            toBePassword:"",
        }
    },
    watch: {

    },
    updated() {

    },
    computed: {

    },
    methods: {
        async resetPassword(){
            // member/reset-password
            try {
                const resetData = {
                    email:this.email,
                    pw:this.asIsPassword,
                    newPw:this.toBePassword
                }
                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member-service/member/reset-password`, resetData)
                console.log(response.data);
                alert("비밀번호 변경 완료")
            } catch (e) {
                console.log(e);
                alert(e || "입력값을 확인해주세요")
            }
        },
        closeModal(){
            // this.emit은 vue에서 컴포넌트간의 이벤트를 전달하는 메커니즘
            // 자식컴포넌트에서 this.$emit을 호출하면 update:dialog라는 이벤트가 실행되고, false를 부모 컴포넌트로 전달
            this.$emit('update:dialog',false)
        }
    }
}
</script>