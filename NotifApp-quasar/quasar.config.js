export default () => ({
  boot: [],

  css: ['app.scss'],

  extras: ['roboto-font', 'material-icons'],

  build: {
    target: {
      browser: ['es2022', 'firefox115', 'chrome115', 'safari14'],
      node: 'node20'
    },
    // Capacitor serve ficheiros locais: o router tem de ser em modo hash
    vueRouterMode: 'hash'
  },

  devServer: {
    open: false
  },

  framework: {
    config: {},
    plugins: ['Notify']
  },

  animations: [],

  capacitor: {
    hideSplashscreen: false
  }
})
