<template>
    <v-app-bar app dark>
        <v-container>
            <v-row align="center">
                <v-col class="d-flex justify-start">
                    <div v-if="userRole === 'ADMIN'">
                    <!-- 왼쪽 정렬 -->
                    <v-btn :to="{path:'/member/list'}">회원관리</v-btn>
                    <v-btn :to="{path:'/product/manage'}">상품관리</v-btn>
                    <v-btn href="/order/list">실시간주문({{ liveQuantity }})</v-btn>
                    </div>
                </v-col>
                <v-col class="text-center">
                    <!-- 가운데 정렬 -->
                    <v-btn :to="{path:'/'}">java shop</v-btn>
                </v-col>
                <v-col class="d-flex justify-end">
                    <!-- 오른쪽 정렬 -->
                    <v-btn v-if="isLogin" :to="{path:'/order/cart'}">장바구니({{ getTotalQuantity }})</v-btn>
                    <v-btn :to="{path:'/product/list'}">상품목록</v-btn>
                    <v-btn v-if="isLogin" :to="{path:'/mypage'}">마이페이지</v-btn>
                    <v-btn v-if="!isLogin" :to="{path:'/member/create'}">회원가입</v-btn>
                    <v-btn v-if="!isLogin" :to="{path:'/login'}">로그인</v-btn>
                    <v-btn v-if="isLogin" @click="doLogout">로그아웃</v-btn>
                </v-col>
            </v-row>
        </v-container>
    </v-app-bar>
</template>

<script>
import { mapGetters } from 'vuex';
// 서버와 실시간 알림 서비스를 위한 의존성 추가 필요
import {EventSourcePolyfill} from 'event-source-polyfill';
export default { 
    data(){
        return{
            userRole: null,
            isLogin: false,
            liveQuantity: 0 // 실시간 주문 개수
        }
    },
    computed:{
        ...mapGetters(['getTotalQuantity'])
    },
    created(){
        // 현재 로그인한 사용자가 일반user인지 admin인지 확인하기 위해서 created될때 localStorage
        const token = localStorage.getItem("token");
        if(token){
            this.isLogin = true;
            this.userRole = localStorage.getItem("role");
        }
        if(this.userRole === 'ADMIN'){
            let sse = new EventSourcePolyfill(`${process.env.VUE_APP_API_BASE_URL}/order-service/subscribe`, {headers: {Authorization: `Bearer ${token}`}});
            sse.addEventListener('connect', (event)=> {
                console.log(event)
            })
            sse.addEventListener('ordered', (event)=> {
                console.log(event.data)
                this.liveQuantity ++;
            })
            sse.onerror = (error) => {
                console.log(error);
                sse.close();
            }
        }
    },
    methods:{
        doLogout(){
            localStorage.clear(); // localStorage에 저장된 토큰, user정보 삭제
            window.location.reload(); // 재시작
        }
    }
};
</script>