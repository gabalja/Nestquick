<script setup>
import { ref } from 'vue';
import MapKakao from "../../stores/map";
import { useRouter } from "vue-router";

const router = useRouter()
</script>

<template>
  <div id="map" style="width:1000px;height:1000px;">
    지도 레쓰고
    <MapKakao :latitude="37.39843974939604" :longitude="127.10972941510465" />
  </div>
</template>

<style scoped>

</style>