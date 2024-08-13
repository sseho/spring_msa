<template>
    <!-- 문자열로 넘길경우에는 변수명=, 그외에는 :변수명= -->
    <ProductListComponent 
        :isAdmin="false"
        pageTitle="상품목록"
    />
</template>

<script>
import ProductListComponent from '@/components/ProductListComponent.vue';
    export default{
        components:{
            ProductListComponent
        },
        data(){
            return{
                
            }
        },
        created(){

        },
        watch:{
            
        },
        updated() {

        },
        computed: {
            
        },
        methods:{

        }
    }
</script>