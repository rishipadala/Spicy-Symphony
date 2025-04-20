'use strict';


/**
 * PRELOAD
 * 
 * loading will be end after document is loaded
 */

const preloader = document.querySelector("[data-preaload]");

window.addEventListener("load", function () {
  preloader.classList.add("loaded");
  document.body.classList.add("loaded");
});



/**
 * add event listener on multiple elements
 */

const addEventOnElements = function (elements, eventType, callback) {
  for (let i = 0, len = elements.length; i < len; i++) {
    elements[i].addEventListener(eventType, callback);
  }
}



/**
 * NAVBAR
 */

const navbar = document.querySelector("[data-navbar]");
const navTogglers = document.querySelectorAll("[data-nav-toggler]");
const overlay = document.querySelector("[data-overlay]");

const toggleNavbar = function () {
  navbar.classList.toggle("active");
  overlay.classList.toggle("active");
  document.body.classList.toggle("nav-active");
}

addEventOnElements(navTogglers, "click", toggleNavbar);



/**
 * HEADER & BACK TOP BTN
 */

const header = document.querySelector("[data-header]");
const backTopBtn = document.querySelector("[data-back-top-btn]");

let lastScrollPos = 0;

const hideHeader = function () {
  const isScrollBottom = lastScrollPos < window.scrollY;
  if (isScrollBottom) {
    header.classList.add("hide");
  } else {
    header.classList.remove("hide");
  }

  lastScrollPos = window.scrollY;
}

window.addEventListener("scroll", function () {
  if (window.scrollY >= 50) {
    header.classList.add("active");
    backTopBtn.classList.add("active");
    hideHeader();
  } else {
    header.classList.remove("active");
    backTopBtn.classList.remove("active");
  }
});



/**
 * HERO SLIDER
 */

const heroSlider = document.querySelector("[data-hero-slider]");
const heroSliderItems = document.querySelectorAll("[data-hero-slider-item]");
const heroSliderPrevBtn = document.querySelector("[data-prev-btn]");
const heroSliderNextBtn = document.querySelector("[data-next-btn]");

let currentSlidePos = 0;
let lastActiveSliderItem = heroSliderItems[0];

const updateSliderPos = function () {
  lastActiveSliderItem.classList.remove("active");
  heroSliderItems[currentSlidePos].classList.add("active");
  lastActiveSliderItem = heroSliderItems[currentSlidePos];
}

const slideNext = function () {
  if (currentSlidePos >= heroSliderItems.length - 1) {
    currentSlidePos = 0;
  } else {
    currentSlidePos++;
  }

  updateSliderPos();
}

heroSliderNextBtn.addEventListener("click", slideNext);

const slidePrev = function () {
  if (currentSlidePos <= 0) {
    currentSlidePos = heroSliderItems.length - 1;
  } else {
    currentSlidePos--;
  }

  updateSliderPos();
}

heroSliderPrevBtn.addEventListener("click", slidePrev);

/**
 * auto slide
 */

let autoSlideInterval;

const autoSlide = function () {
  autoSlideInterval = setInterval(function () {
    slideNext();
  }, 7000);
}

addEventOnElements([heroSliderNextBtn, heroSliderPrevBtn], "mouseover", function () {
  clearInterval(autoSlideInterval);
});

addEventOnElements([heroSliderNextBtn, heroSliderPrevBtn], "mouseout", autoSlide);

window.addEventListener("load", autoSlide);



/**
 * PARALLAX EFFECT
 */

const parallaxItems = document.querySelectorAll("[data-parallax-item]");

let x, y;

window.addEventListener("mousemove", function (event) {

  x = (event.clientX / window.innerWidth * 10) - 5;
  y = (event.clientY / window.innerHeight * 10) - 5;

  // reverse the number eg. 20 -> -20, -5 -> 5
  x = x - (x * 2);
  y = y - (y * 2);

  for (let i = 0, len = parallaxItems.length; i < len; i++) {
    x = x * Number(parallaxItems[i].dataset.parallaxSpeed);
    y = y * Number(parallaxItems[i].dataset.parallaxSpeed);
    parallaxItems[i].style.transform = `translate3d(${x}px, ${y}px, 0px)`;
  }

});

/**
 * BLOG
 */

document.addEventListener('DOMContentLoaded', function () {
  const loginBtns = document.querySelectorAll('.btn-blog');
  loginBtns.forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      alert('Blog Section - Coming soon!');
    });
  });
});

/**
 * Order 
 */

document.addEventListener('DOMContentLoaded', function () {
  const loginBtns = document.querySelectorAll('.btn-order');
  loginBtns.forEach(btn => {
    btn.addEventListener('click', (e) => {
      e.preventDefault();
      alert('Online Order - Coming soon!');
    });
  });
});



/**
 * RESERVATION FORM
 */


document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("reservation-form");
  const confirmationMessage = document.getElementById("confirmation-message");
  const okButton = document.getElementById("ok-button");


  if (!form) {
      console.error("❌ Error: Form with ID 'reservation-form' not found.");
      return;
  }

  console.log("✅ Form found:", form); // Debugging log

  const nameField = document.getElementById("name");
  const phoneField = document.getElementById("phone");
  const emailField = document.getElementById("email");
  const dateField = document.getElementById("reservation-date");
  const timeField = document.getElementById("time");
  const personsField = document.getElementById("persons");
  const messageField = document.getElementById("message");

  // Log missing fields to console
  if (!nameField) console.error("❌ Error: Name field is missing.");
  if (!phoneField) console.error("❌ Error: Phone field is missing.");
  if (!emailField) console.error("❌ Error: Email field is missing.");
  if (!dateField) console.error("❌ Error: Date field is missing.");
  if (!timeField) console.error("❌ Error: Time field is missing.");
  if (!personsField) console.error("❌ Error: Persons field is missing.");
  if (!messageField) console.warn("⚠️ Warning: Message field is missing (optional).");

  if (!nameField || !phoneField || !emailField ||!dateField || !timeField || !personsField) {
      console.error("❌ Error: One or more form elements are missing.");
      return;
  }

  console.log("✅ All form fields found!");

  // Restrict Name Field to Alphabets Only
  nameField.addEventListener("input", function () {
      this.value = this.value.replace(/[^A-Za-z\s]/g, ""); // Remove non-alphabet characters
  });

  // Restrict Phone Number Field to Numbers Only
  phoneField.addEventListener("input", function () {
      this.value = this.value.replace(/\D/g, ""); // Remove non-numeric characters

      // Limit to 10 digits after +91
    if (numericValue.length > 10) {
        numericValue = numericValue.slice(0, 10);
    }

    // Format the number as "+91 XXXXX XXXXX"
    if (numericValue.length <= 5) {
        this.value = "+91 " + numericValue;
    } else {
        const firstPart = numericValue.slice(0, 5);
        const secondPart = numericValue.slice(5);
        this.value = "+91 " + firstPart + " " + secondPart;
    }
});
  
  // This function for email validation
  emailField.addEventListener("input", function isValidEmail(email) {
    const isValid = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailField.value);
    if (!isValid) {
    // Optionally, show some error message or style the field accordingly, e.g., add a red border
    emailField.style.borderColor = "red";
  } else {
    emailField.style.borderColor = ""; // Reset if valid
  }
  });

  // Handle Form Submission
  form.addEventListener("submit", async function (event) {
      event.preventDefault(); // Prevent default form submission

      // Check if the disclaimer checkbox is ticked
     const disclaimerAgree = document.getElementById("disclaimer-agree");
     if (!disclaimerAgree.checked) {
        alert("Please agree to the reservation policy before submitting.");
        return;
    }

      console.log("Form submission triggered"); // Log each attempt

      // Capture and format phone number
    let phoneValue = phoneField.value.replace(/\D/g, ""); // Get digits only
    if (phoneValue.length !== 10) {
        alert("Phone number must be 10 digits.");
        return;
    }
    const formattedPhone = "+91 " + phoneValue.slice(0, 5) + " " + phoneValue.slice(5);

      // Capture form data
      const formData = {
          name: nameField.value.trim(),
          phone: formattedPhone, // Send in "+91 XXXXX XXXXX" format
          email: emailField.value.trim(),
          date: dateField.value,
          time: timeField.value,
          persons: parseInt(personsField.value.split("-")[0]),
          message: messageField.value.trim()
      };

      console.log("Sending form data:", formData); // Log the data being sent

      // Simple form validation
      if (!formData.name || !formData.phone || !formData.email || !formData.date || !formData.time || !formData.persons) {
          alert("Please fill in all required fields correctly.");
          return;
      }

      try {
          // Send data to backend
          const response = await fetch("http://localhost:8080/api/reservations", {
              method: "POST",
              headers: {
                  "Content-Type": "application/json"
              },
              body: JSON.stringify(formData)
          });

          console.log("Response status:", response.status); // Log the response

          if (response.ok) {
            // Show modal
            document.getElementById("overlay").style.display = "block";
            document.getElementById("confirmation-message").style.display = "block";
            document.getElementById("confirmation-message").classList.add("show");
      
            // Hide modal after 20 seconds (optional)
            setTimeout(() => {
              document.getElementById("overlay").style.display = "none";
              document.getElementById("confirmation-message").style.display = "none";
              document.getElementById("confirmation-message").classList.remove("show");
            }, 20000);

              form.reset(); // Reset form after successful submission
          } else {
            // Error case: Fetch response text for detailed message
            const errorText = await response.text();
            console.error("Error response:", errorText);
      
            if (response.status === 400) {
              alert(errorText || "Email or phone number already exists.");
            } else if (response.status === 500) {
              alert(errorText || "Server error. Please try later.");
            } else {
              alert("Error in booking: " + errorText);
            }
          }
      } catch (error) {
          console.error("Error:", error);
          alert("Server error. Please try later.");
      }
      if (okButton) {
        okButton.addEventListener("click", function() {
          overlay.classList.remove("show");
          confirmationMessage.classList.remove("show");
        });
      }
  });
});

/**
 * TESTIMONIALS SLIDER
 */
const slides = document.querySelector('.slides');
const totalSlides = document.querySelectorAll('.slide').length;
let slideIndex = 0;

function moveSlide(direction) {
  if (!slides) return;
  slideIndex += direction;
  if (slideIndex >= totalSlides) slideIndex = 0;
  else if (slideIndex < 0) slideIndex = totalSlides - 1;
  slides.style.transform = `translateX(-${slideIndex * 100}%)`;
}

setInterval(() => moveSlide(1), 5000);






