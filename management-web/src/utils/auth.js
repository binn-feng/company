import Cookies from 'js-cookie'
import {
  ACCESS_NAME,
  ACCESS_USERNAME,
  ACCESS_ID,
  ACCESS_AREAID,
  ACCESS_ROLE,
  ACCESS_ROUTES,
  ACCESS_SYSTEMNAME,
  ACCESS_READONLY
} from './constants'

const TokenKey = 'vue_admin_template_token'
// token
export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
// name
export function setName(name) {
  return localStorage.setItem(ACCESS_NAME, name)
}
export function getName(name) {
  return localStorage.getItem(ACCESS_NAME, name)
}
export function removeName(name) {
  return localStorage.removeItem(ACCESS_NAME, name)
}
// username
export function setUserName(username) {
  return localStorage.setItem(ACCESS_USERNAME, username)
}
export function getUserName(username) {
  return localStorage.getItem(ACCESS_USERNAME, username)
}
export function removeUserName(username) {
  return localStorage.removeItem(ACCESS_USERNAME, username)
}
// id
export function setUserID(id) {
  return localStorage.setItem(ACCESS_ID, id)
}
export function getUserID(id) {
  return localStorage.getItem(ACCESS_ID, id)
}
export function removeUserID(id) {
  return localStorage.removeItem(ACCESS_ID, id)
}
// areaid
export function serUserAreaID(id) {
  return localStorage.setItem(ACCESS_AREAID, id)
}
export function getUserAreaID(id) {
  return localStorage.getItem(ACCESS_AREAID, id)
}
export function removeUserAreaID(id) {
  return localStorage.removeItem(ACCESS_AREAID, id)
}
// role
export function setRole(role) {
  return localStorage.setItem(ACCESS_ROLE, role)
}
export function getRole(role) {
  return localStorage.getItem(ACCESS_ROLE, role)
}
export function removeRole(role) {
  return localStorage.removeItem(ACCESS_ROLE, role)
}

export function setRoutes(routes) {
  return localStorage.setItem(ACCESS_ROUTES, JSON.stringify(routes))
}
export function getRoutes(routes) {
  return localStorage.getItem(ACCESS_ROUTES, routes)
}

export function setSystemName(systemnames) {
  return localStorage.setItem(ACCESS_SYSTEMNAME, systemnames)
}
export function getSystemNames(systemnames) {
  return localStorage.getItem(ACCESS_SYSTEMNAME, systemnames)
}
export function setReadOnly(readonly) {
  return localStorage.setItem(ACCESS_READONLY, readonly)
}
export function getReadOnly(readonly) {
  return localStorage.getItem(ACCESS_READONLY, readonly)
}
export function removeReadOnly(readonly) {
  return localStorage.removeItem(ACCESS_READONLY, readonly)
}
