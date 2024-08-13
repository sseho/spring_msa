<template>
  <!-- <HelloWorld /> -->
  <!--router-view를 통해 app.vue에서 화면 라우팅  -->

  <v-app>
    <HeaderComponent />
    <v-main>
      <router-view />
    </v-main>
    <FooterComponent />
  </v-app>
</template>
<!-- template에는html코드가 들어가고, script에는 js코드가 들어감.디자인은 style태그안에 들어감 -->
<!-- vue파일 안에는 head태그가 없고, head의 공통 import사항은 main.js에 구성 -->
<script>
  // app.vue는 vue애플리케이션의 루트 컴포넌트(재활용이 가능한 화면)
  // 화면전환을 라우팅이라 한다
  // import HelloWorld from './components/HelloWorld.vue'
  import HeaderComponent from './components/HeaderComponent.vue'
  import FooterComponent from './components/FooterComponent.vue' 
  export default {
    name: 'App',
    components: {
      HeaderComponent,
      FooterComponent
    }
  }
</script>

<style>

</style>