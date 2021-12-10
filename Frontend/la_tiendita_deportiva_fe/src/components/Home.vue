<template>
<div class="banner">
    <div class="dj-bg">
        <img src = "@/assets/1.jpg">
        <div class="dj-text">
            <h1><b>Super Oferta</b></h1>
            <p>50% descuento</p>
        </div>
    </div>            
</div>
</template>
    
<script>
    export default {
    }
</script>
    
<style>
   .banner{margin: 0; width: 100%; position: relative; border: 0px solid #283747;}
   .banner .dj-bg img{width: 100%;}
   .banner .dj-text{text-align: center; padding-bottom: 20px;}
   @media screen and (min-width:768px){
      .banner .dj-text{position: absolute; top: 50%; left: 10%; transform: translate(-10%, -50%); width: 50%; text-align: left; color: white;}
      .banner .dj-text h1{font-size: calc(2*1.5vw);}
      .banner .dj-text p{font-size: calc(1.2*1.5vw);}
   }
</style>

