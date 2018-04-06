%vistyl handler
imgedge = csvread('image_edge.csv');
imgorg = csvread('image_org.csv');
%
figure,
subplot(1,2,1)%figure(i)
surf(imgedge);
%surf(flipud(imgB'));
title('imgedge');
colormap(gray);
axis equal;
shading interp;
view(2);
set(gcf,'Renderer','Zbuffer')   % Do this to get rid of white pixel artifacts created from view(2)
colorbar('horiz');

subplot(1,2,2)%figure(i)
surf(imgorg);
%surf(flipud(imgB'));
title('imgorg');
colormap(gray);

axis equal;
shading interp;
view(2);
set(gcf,'Renderer','Zbuffer')   % Do this to get rid of white pixel artifacts created from view(2)
colorbar('horiz');
%%
%run.pl handler
i=42;
figure(i)
surf(imgorg);
%surf(flipud(imgB'));
title('imgorg');
colormap(gray);

axis equal;
shading interp;
view(2);
set(gcf,'Renderer','Zbuffer')   % Do this to get rid of white pixel artifacts created from view(2)
colorbar('horiz');
%%
[SHARPNESS,imgOutSharpOrg] = sobel_sharpness(imgorg)
subplot(1,3,3)%figure(i)
surf(imgOutSharpOrg);
%surf(flipud(imgB'));
title('imgOutSharpOrg');
colormap(gray);

axis equal;
shading interp;
view(2);
set(gcf,'Renderer','Zbuffer')   % Do this to get rid of white pixel artifacts created from view(2)
colorbar('horiz');



%%
figure%figure(i+1)
surf(imgOut3);
%surf(flipud(imgB'));
title('img')
colormap(gray)

axis equal
shading interp
view(2);
set(gcf,'Renderer','Zbuffer')   % Do this to get rid of white pixel artifacts created from view(2)
colorbar('horiz');
%%
gxx = 0;
gxy = 0;
gyy = 0;

num_rows = size(img,1);
num_cols = size(img,2);

 for i=2:num_rows
    for j=2:num_cols
        
        gx = (img(i*num_cols + j + 1) - img(i*num_cols + j - 1))/2;
        gy = (img((i + 1)*num_cols + j) - img((i - 1)*num_cols + j))/2;
        Gx(i-1,j-1) = gx;
       
    end
 end

%%
% Calculate SNR of frame f.
%f = randn(56, 144); % Random noise
f=img;
[fx, fy] = gradient(f);
[~, S, ~] = svd([fx(:) fy(:)], 'econ');
snr = S(1,1)/S(2,2)