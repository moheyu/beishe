import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getScenicCategoryList } from '@/api/scenic'
import { getRouteCategoryList } from '@/api/route'
import { getTagList } from '@/api/tag'

export const useCacheStore = defineStore('cache', () => {
  // State
  const scenicCategories = ref([])
  const routeCategories = ref([])
  const tags = ref([])
  
  // Actions
  const fetchScenicCategories = async (force = false) => {
    if (!force && scenicCategories.value.length > 0) {
      return scenicCategories.value
    }
    const data = await getScenicCategoryList()
    scenicCategories.value = data || []
    return scenicCategories.value
  }
  
  const fetchRouteCategories = async (force = false) => {
    if (!force && routeCategories.value.length > 0) {
      return routeCategories.value
    }
    const data = await getRouteCategoryList()
    routeCategories.value = data || []
    return routeCategories.value
  }
  
  const fetchTags = async (force = false) => {
    if (!force && tags.value.length > 0) {
      return tags.value
    }
    const data = await getTagList()
    tags.value = data || []
    return tags.value
  }
  
  const clearCache = () => {
    scenicCategories.value = []
    routeCategories.value = []
    tags.value = []
  }
  
  return {
    scenicCategories,
    routeCategories,
    tags,
    fetchScenicCategories,
    fetchRouteCategories,
    fetchTags,
    clearCache
  }
})
