{ pkgs ? import <nixpkgs> {} }:
let
      x86-darwin-pkgs = import <nixpkgs> {
        system = "x86_64-darwin";
        config = { allowUnfree = true; };
      };
in
  pkgs.mkShell {
    nativeBuildInputs = [ pkgs.sqlite ];
}
