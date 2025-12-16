# Launcher Icons

This directory should contain the launcher icons for the app.

## Required Icons

Place the following icons in their respective directories:

### For all densities:
- `ic_launcher.png` - Standard launcher icon
- `ic_launcher_round.png` - Round launcher icon (for devices that use circular icons)

### Icon Sizes:
- **mdpi**: 48x48 px
- **hdpi**: 72x72 px  
- **xhdpi**: 96x96 px
- **xxhdpi**: 144x144 px
- **xxxhdpi**: 192x192 px

## Creating Icons

### Option 1: Use Android Studio's Image Asset Tool
1. Right-click on `res` folder
2. Select **New → Image Asset**
3. Choose **Launcher Icons (Adaptive and Legacy)**
4. Upload your icon image (512x512 px recommended)
5. Adjust settings as needed
6. Click **Next** and **Finish**

### Option 2: Use Online Tools
- [Android Asset Studio](https://romannurik.github.io/AndroidAssetStudio/)
- [App Icon Generator](https://appicon.co/)
- [Icon Kitchen](https://icon.kitchen/)

### Option 3: Manual Creation
Create PNG files of the appropriate sizes and place them in each mipmap directory.

## Temporary Solution

Until custom icons are added, the app will use Android's default system icon. This is acceptable for development but should be replaced before release.

## Design Guidelines

Follow [Material Design icon guidelines](https://m3.material.io/styles/icons/overview):
- Use simple, recognizable shapes
- Ensure good contrast
- Test on both light and dark backgrounds
- Consider adaptive icon support (Android 8.0+)
