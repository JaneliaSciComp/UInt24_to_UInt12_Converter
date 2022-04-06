# UInt24_to_UInt12_Converter
FIJI ImageJ 1 Plugin to convert 24-bit pixels to 2x 12-bit pixels

## Running

1. Open a file with data encoded as `uint24` or 24-bit pixels. For example, File -> Import -> HDF5
2. Run `Plugins` > `UInt24toUInt12Converter Plugin`

## Description

Some scientific cameras produce packed 12-bit data (e.g. MONO12P from Hamamatsu).
In particular, 12-bit depth allows for a good compromise between speed and dynamic range.
This format can be difficult to encode and decode using standard tools. However, the data can be made readable with half the pixels in the form of 24-bit data.
Since 24-bit data is a multiple of 8-bit bytes, this form can be more easily encoded and decoded by a wide range of software tools.

HDF5, for example, can label a dataset as having unsigned 24-bit integers. This can then be loaded into an updated FIJI as a 32-bit integer image, displaying half the pixels.
Each pixel is a combination of two pixels, with even pixel values multiplied by 4096 (or left shifted by 12-bits).

This plugin helps to separate the 24-bit pixels into 2 12-bit pixels according to the following psuedocode.
```
short odd_pixel = twentyfour_bit_pixel & 0xfff;
short even_pixel = twentyfour_bit_pixel >> 12;
```

The plugin is currently in the form of a ImageJ1 plugin. It extends `ij.plugin.filter.PlugInFilter`.

Internally, the plugin uses [`imglib2`](https://github.com/imglib/imglib2) and in particular [`imglib2-ij`](https://github.com/imglib/imglib2-ij).
It currently depends on `imglib2-ij-2.0.0-beta-47-SNAPSHOT` with https://github.com/imglib/imglib2-ij/pull/31 that adds `ImagePlusAdapter.wrapInt`.
