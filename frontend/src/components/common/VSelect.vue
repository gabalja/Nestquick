<script setup>
import { ref } from 'vue'
defineProps({ selectOption: Array })
const emit = defineEmits(['onKeySelect'])

const key = ref('')

const onSelect = () => {
  // console.log(key.value + "선택!!!");
  emit('onKeySelect', key.value, '여기에 전달')
}
</script>

<template>
  <select v-model="key" class="form-select form-select-sm w-50" @change="onSelect">
    <option
      v-for="option in selectOption"
      :key="option.value"
      :value="option.value"
      :disabled="option.value === '' ? true : false"
    >
      {{ option.text }}
    </option>
  </select>
</template>

<style scoped></style>
