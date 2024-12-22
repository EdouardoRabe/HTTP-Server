var animal=document.querySelector("select");
function affiche(){
    var tab=new Array();
    tab[0]="2.jpg";
    tab[1]="1.jpg";
    tab[2]="25.jpg";
    tab[3]="4.jpg";
    var image=document.querySelector("img");
    image.src=tab[animal.value];
    image.width=100;
    image.height=100;
}