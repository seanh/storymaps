#!/usr/bin/perl -w
# Program to generate a PDF file with PNG images 
# using a pre-defined layout with ImageMagick's
# montage tool.


# Directory with the PNG files
my $dir = ".";
my $images_per_page = 4; # has to be even

# This can be redefined if you want a different layout, by default
# it sets the images in a square
my $row = $images_per_page/2 ;
my $col = $images_per_page/2 ;

opendir (DIR, $dir) || die "Cannot open dir $dir: $!";

@files= sort(grep { /\.png$/ && -f "$dir/$_" } readdir(DIR));
closedir DIR;

$countfiles = $#files + 1;
$total_pages = sprintf("%.0f", $countfiles / $images_per_page);
$total_pages ++ if $countfiles % $images_per_page  != 0 ;
print STDERR "There are $countfiles files. With $images_per_page, I will generate $total_pages pages\n";

$pagenum = 0;
for ($pagenum = 0;  $pagenum < $total_pages ; $pagenum++) {
    $file_list = "";
    for ($i = 0 ; $i < $images_per_page; $i ++ ) {
        if ( $#files >= 0 ) {
            $file_list = $file_list." ".(pop @files);
        }
    } 
    $command = "montage -mode concatenate -borderwidth 10 -bordercolor white -tile ${col}x${row} $file_list page${pagenum}.pdf";
    print STDERR "Running: $command\n";
    `$command`;
}

exit 0;
