<script setup>
import { ref } from 'vue'
defineProps({ selectOption: Array })
const emit = defineEmits(['onFilterSelect'])

const filter = ref('')

const onSelect = () => {
  emit('onFilterSelect', filter.value)
}
</script>

<template>
  <select class="form-select form-select" @change="onSelect" v-model="filter">
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
