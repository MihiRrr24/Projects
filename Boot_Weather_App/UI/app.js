// ======= CONFIG =======
const API_BASE = "http://localhost:8080/weather/forecast";

// ======= DOM =======
const form = document.getElementById("query-form");
const cityInput = document.getElementById("city");
const daysSelect = document.getElementById("days");
const currentCard = document.getElementById("current");
const locationEl = document.getElementById("location");
const conditionEl = document.getElementById("condition");
const temperatureEl = document.getElementById("temperature");
const currentIcon = document.getElementById("current-icon");
const forecastGrid = document.getElementById("forecast");
const themeToggle = document.getElementById("theme-toggle");

// ======= THEME =======
const savedTheme = localStorage.getItem("theme") || "dark";
document.body.classList.toggle("light", savedTheme === "light");
themeToggle.addEventListener("click", () => {
  const isLight = document.body.classList.toggle("light");
  localStorage.setItem("theme", isLight ? "light" : "dark");
});

// ======= UTIL: ICONS =======
function iconFor(conditionText) {
  const t = (conditionText || "").toLowerCase();
  const isThunder = /thunder|storm/.test(t);
  const isSnow = /snow|sleet|blizzard|flurr/.test(t);
  const isRain = /rain|drizzle|shower/.test(t);
  const isCloudy = /cloud|overcast/.test(t);
  const isFog = /mist|fog|haze|smoke/.test(t);
  const isClear = /clear|sunny/.test(t);

  if (isThunder) return SVG_THUNDER();
  if (isSnow) return SVG_SNOW();
  if (isRain) return SVG_RAIN();
  if (isFog) return SVG_FOG();
  if (isCloudy && isClear) return SVG_PARTLY_CLOUDY();
  if (isCloudy) return SVG_CLOUDY();
  if (isClear) return SVG_SUN();
  return SVG_WEATHER();
}

// ======= FETCH + RENDER =======
async function fetchForecast(city, days) {
  const url = `${API_BASE}?city=${encodeURIComponent(city)}&days=${encodeURIComponent(days)}`;
  setLoading(true);
  try {
    const res = await fetch(url);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    return await res.json();
  } finally {
    setLoading(false);
  }
}

function setLoading(isLoading) {
  currentCard.classList.toggle("skeleton", isLoading);
  if (isLoading) {
    forecastGrid.classList.remove("empty-state");
    forecastGrid.innerHTML = "";
    for (let i = 0; i < 8; i++) {
      const card = document.createElement("div");
      card.className = "forecast-card card skeleton";
      card.style.height = "120px";
      forecastGrid.appendChild(card);
    }
  }
}

function render(data) {
  const w = data.weatherResponse || {};
  locationEl.textContent = `${w.city ?? "—"}, ${w.region ?? ""} ${w.country ? "· " + w.country : ""}`.trim();
  conditionEl.textContent = w.condition ?? "—";
  temperatureEl.textContent = (w.temperature !== undefined) ? `${w.temperature.toFixed(1)}°C` : "—";
  currentIcon.innerHTML = iconFor(w.condition);

  forecastGrid.classList.remove("empty-state");
  forecastGrid.innerHTML = "";
  (data.dayTemp || []).forEach(day => {
    const card = document.createElement("div");
    card.className = "forecast-card card";

    const top = document.createElement("div");
    top.className = "fc-top";
    const date = new Date(day.date);
    const label = isNaN(date) ? day.date : date.toLocaleDateString(undefined, { weekday: "short", month: "short", day: "numeric" });
    top.innerHTML = `<span>${label}</span><span>${day.avgTemp.toFixed(1)}°C avg</span>`;

    const mid = document.createElement("div");
    mid.className = "fc-mid";
    const icon = document.createElement("div");
    icon.className = "icon-md";
    icon.innerHTML = iconFor((w.condition || "").toLowerCase()); // replace with per-day condition if available
    const temps = document.createElement("div");
    temps.className = "fc-temps";
    temps.innerHTML = `
      <span class="temp max">${day.maxTemp.toFixed(1)}°</span>
      <span class="min">${day.minTemp.toFixed(1)}°</span>
    `;
    mid.appendChild(icon);
    mid.appendChild(temps);

    card.appendChild(top);
    card.appendChild(mid);
    forecastGrid.appendChild(card);
  });
}

function showError(err) {
  currentIcon.innerHTML = SVG_WARNING();
  locationEl.textContent = "Could not load forecast";
  conditionEl.textContent = (err && err.message) ? err.message : "Unknown error";
  temperatureEl.textContent = "—";
  forecastGrid.innerHTML = "";
}

// ======= FORM =======
form.addEventListener("submit", async (e) => {
  e.preventDefault();
  // basic required validation (handles disabled placeholder too)
  if (!cityInput.value.trim() || !daysSelect.value) {
    form.reportValidity?.();
    return;
  }
  try {
    const data = await fetchForecast(cityInput.value.trim(), daysSelect.value);
    render(data);
  } catch (err) {
    showError(err);
  }
});

// ======= NO AUTO-LOAD =======
// waits for the user to enter city + days and click submit

// ======= SVG ICONS =======
function SVG_SUN() {
  return `
<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" aria-hidden="true">
  <circle cx="12" cy="12" r="4.5" fill="currentColor" opacity="0.15"/>
  <circle cx="12" cy="12" r="4.5"/>
  <g stroke-linecap="round">
    <path d="M12 2.5v2.5M12 19v2.5M4.5 12H2M22 12h-2.5M5.5 5.5L4 4M20 20l-1.5-1.5M18.5 5.5L20 4M4 20l1.5-1.5"/>
  </g>
</svg>`;
}
function SVG_CLOUDY() {
  return `
<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" aria-hidden="true">
  <path d="M6 18h10a4 4 0 0 0 .5-7.97A6 6 0 0 0 5 11.5" fill="currentColor" opacity="0.12"/>
  <path d="M6 18h10a4 4 0 0 0 .5-7.97A6 6 0 0 0 5 11.5 3.5 3.5 0 0 0 6 18Z"/>
</svg>`;
}
function SVG_PARTLY_CLOUDY() {
  return `
<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" aria-hidden="true">
  <g opacity="0.9">${SVG_SUN().replace('<svg','<g').replace('</svg>','</g>')}</g>
  <g transform="translate(4,6)">
    ${SVG_CLOUDY().replace('<svg','<g').replace('</svg>','</g>')}
  </g>
</svg>`;
}
function SVG_RAIN() {
  return `
<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" aria-hidden="true">
  <g>${SVG_CLOUDY().replace('<svg','<g').replace('</svg>','</g>')}</g>
  <g stroke-linecap="round">
    <path d="M8 20l1-2M12 20l1-2M16 20l1-2"/>
  </g>
</svg>`;
}
function SVG_THUNDER() {
  return `
<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" aria-hidden="true">
  <g>${SVG_CLOUDY().replace('<svg','<g').replace('</svg>','</g>')}</g>
  <path d="M12 14l-2.5 4H12l-1 3 3-5h-1.5l1-2z" fill="currentColor"/>
</svg>`;
}
function SVG_SNOW() {
  return `
<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" aria-hidden="true">
  <g>${SVG_CLOUDY().replace('<svg','<g').replace('</svg>','</g>')}</g>
  <g stroke-linecap="round">
    <path d="M8 19.5v2M16 19.5v2M12 19.5v2"/>
  </g>
</svg>`;
}
function SVG_FOG() {
  return `
<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" aria-hidden="true">
  <g>${SVG_CLOUDY().replace('<svg','<g').replace('</svg>','</g>')}</g>
  <g stroke-linecap="round">
    <path d="M6 19h12M7 21h10" />
  </g>
</svg>`;
}
function SVG_WEATHER() {
  return `
<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" aria-hidden="true">
  <g>${SVG_PARTLY_CLOUDY().replace('<svg','<g').replace('</svg>','</g>')}</g>
</svg>`;
}
function SVG_WARNING() {
  return `
<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.6" aria-hidden="true">
  <path d="M12 3l9 16H3l9-16z" />
  <path d="M12 9v5" />
  <circle cx="12" cy="17" r="1.1" fill="currentColor" />
</svg>`;
}
