module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        // Couleurs ENICarthage (à adapter selon la charte officielle)
        primary: '#003B7D',      // Bleu ENICarthage
        secondary: '#00A0DC',     // Bleu clair
        accent: '#FFB81C',        // Jaune/Orange
        success: '#10b981',
        warning: '#f59e0b',
        danger: '#ef4444',
      }
    },
  },
  plugins: [],
}