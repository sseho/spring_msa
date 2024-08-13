<template>
    <h1>생명주기 함수</h1>
    <p>{{data1}}</p>
    <p>{{data2}}</p>
    <p>{{data3}}</p>
</template>

<script>
    export default{
        data(){
            return{
                data1: "변경될 데이터1",
                data2: "변경될 데이터2",
                data3: "변경될 데이터3"
                
            }
        },
        // 화면이 열리기전(vue인스턴스가 생성되기 이전)에 실행되는 함수
        // 데이터 받아오기
        created(){
            this.data2 = "변경된 데이터 2"
            console.log("화면이 열리기전 실행");
            
        },
        beforeMount(){
            window.addEventListener('resize',this.resize)
        },
        // vue컴포넌트들이 DOM에 마운트된 이후(화면이 모두 그려진 이후)에 실행되는 함수
        mounted(){
            console.log("화면이 그려진 이후에 출력");
            this.data3 = "변경된 데이터 3"
            
        },
        // 컴포넌트가 화면에서 제거될때 아래 함수 실행. beforeDestory로 사용하기도 함
        // 웹소켓,sse연결 등 서버와의 연결을 끊어주고 나가야하는 시점
        beforeUnmount(){
            window.removeEventListener('resize',this.resize)
        },
        // computed는 종속된 반응형 데이터가 변경될때에 함수를 다시 실행하여 값을 계산하는 함수
        computed: {
            
        },
        methods:{
            resize(){
                alert("화면의 배율이 변경되고 있습니다.")
                
            }
        }
    }
</script>